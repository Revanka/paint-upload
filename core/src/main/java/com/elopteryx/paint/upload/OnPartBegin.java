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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * A functional interface. An implementation of it must be passed in the
 * {@link UploadParser#onPartBegin(OnPartBegin)} onPartBegin} method to call it at the start of parsing for each part.
 */
@FunctionalInterface
public interface OnPartBegin {

    /**
     * The function to implement. When it's called depends on the size threshold.
     * @param context The upload context
     * @param buffer The byte buffer containing the first bytes of the part
     * @return A non-null channel to write out the part
     * @throws IOException If an error occurred with the channel
     */
    WritableByteChannel onPartBegin(UploadContext context, ByteBuffer buffer) throws IOException;

}