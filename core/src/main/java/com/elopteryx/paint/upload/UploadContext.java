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
package com.elopteryx.paint.upload;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.channels.WritableByteChannel;
import java.util.List;

/**
 * The context object which is passed to the user-supplied functions,
 * allowing the user to get the necessary objects during the various
 * stages of the parsing.
 */
public interface UploadContext {

    /**
     * Returns the given request object for this parser,
     * allowing customization during the stages of the
     * asynchronous processing.
     *
     * @return The request object
     */
    @Nonnull
    HttpServletRequest getRequest();

    /**
     * Returns the given response object for this parser,
     * allowing customization during the stages of the
     * asynchronous processing.
     *
     * @return The response object
     */
    @Nonnull
    HttpServletResponse getResponse();

    /**
     * Returns the currently processed part stream,
     * allowing the caller to get available information.
     *
     * @return The currently processed part
     */
    @Nonnull
    PartStream getCurrentPart();

    /**
     * Returns the currently active channel, which was returned
     * in the latest UploadParser#onPartBegin method. This will
     * return a null during the part begin stage and might be
     * null during the error stage.
     *
     * @return The latest channel set by the user
     */
    @Nullable
    WritableByteChannel getCurrentChannel();

    /**
     * Returns the parts which have already been processed. Before
     * the onPartBegin method is called the current PartStream is
     * added into the List returned by this method, meaning that
     * the UploadContext#getCurrentPart will return with the last
     * element of the list.
     * @return The list of the processed parts, in the order they are uploaded
     */
    @Nonnull
    List<PartStream> getPartStreams();
}