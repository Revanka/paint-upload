/*
 * Copyright (C) 2015 Adam Forgacs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elopteryx.paint.upload.rs;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import com.elopteryx.paint.upload.OnError;
import com.elopteryx.paint.upload.OnPartBegin;
import com.elopteryx.paint.upload.OnPartEnd;
import com.elopteryx.paint.upload.OnRequestComplete;
import com.elopteryx.paint.upload.PartOutput;
import com.elopteryx.paint.upload.UploadContext;
import com.elopteryx.paint.upload.errors.MultipartException;
import com.elopteryx.paint.upload.errors.RequestSizeException;
import com.elopteryx.paint.upload.impl.AbstractUploadParser;
import com.elopteryx.paint.upload.impl.MultipartParser;
import com.elopteryx.paint.upload.impl.NullChannel;
import com.elopteryx.paint.upload.impl.PartStreamHeaders;
import com.elopteryx.paint.upload.impl.UploadContextImpl;

import java.io.InputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

/**
 * This class is an example of a message body reader for multipart requests. It works like the blocking
 * upload parser.
 */
@Provider
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class UploadReader extends AbstractUploadParser<UploadReader> implements MessageBodyReader<UploadContext>, OnPartBegin, OnPartEnd, OnRequestComplete, OnError {

    private static final String CONTENT_LENGTH = "Content-Length";

    private static final String CONTENT_ENCODING = "Content-Encoding";

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return mediaType.getType().equals("multipart");
    }

    @Override
    public UploadContext readFrom(Class<UploadContext> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                      MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {


        partBeginCallback = this;
        partEndCallback = this;

        //Fail fast mode
        if (maxRequestSize > -1) {
            long requestSize = Long.valueOf(httpHeaders.getFirst(CONTENT_LENGTH));
            if (requestSize > maxRequestSize) {
                throw new RequestSizeException("The size of the request (" + requestSize
                        + ") is greater than the allowed size (" + maxRequestSize + ")!", requestSize, maxRequestSize);
            }
        }

        checkBuffer = ByteBuffer.allocate(sizeThreshold);
        context = new UploadContextImpl(null, uploadResponse);

        String mimeType = httpHeaders.getFirst(PartStreamHeaders.CONTENT_TYPE);
        String boundary;
        if (mimeType != null && mimeType.startsWith(MULTIPART_FORM_DATA)) {
            boundary = PartStreamHeaders.extractBoundaryFromHeader(mimeType);
            if (boundary == null) {
                throw new RuntimeException("Could not find boundary in multipart request with ContentType: " + mimeType + ", multipart data will not be available");
            }
            Charset charset = httpHeaders.getFirst(CONTENT_ENCODING) != null ? Charset.forName(httpHeaders.getFirst(CONTENT_ENCODING)) : ISO_8859_1;
            parseState = MultipartParser.beginParse(this, boundary.getBytes(), charset);
        }

        try {
            while (true) {
                int count = entityStream.read(buf);
                if (count == -1) {
                    if (!parseState.isComplete()) {
                        throw new MultipartException();
                    } else {
                        break;
                    }
                } else if (count > 0) {
                    checkRequestSize(count);
                    parseState.parse(ByteBuffer.wrap(buf, 0, count));
                }
            }
            onRequestComplete(context);
        } catch (Exception e) {
            onError(context, e);
        }
        return context;
    }

    @Nonnull
    @Override
    public PartOutput onPartBegin(UploadContext context, ByteBuffer buffer) throws IOException {
        // Intentionally written to return a null channel, subclasses should override it
        return PartOutput.from(new NullChannel());
    }

    @Override
    public void onPartEnd(UploadContext context) throws IOException {
        // Intentionally left empty, subclasses can optionally override it
    }

    @Override
    public void onRequestComplete(UploadContext context) throws IOException, ServletException {
        // Intentionally left empty, subclasses should override it
    }

    @Override
    public void onError(UploadContext context, Throwable throwable) {
        // Intentionally left empty, subclasses can optionally override it
    }
}