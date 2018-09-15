/*
 * Copyright (C) 2016 Adam Forgacs
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

package com.github.elopteryx.upload.rs;

import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

/**
 * This interface represents a multipart request.
 */
public interface MultiPart {

    /**
     * Returns the list of the received and processed
     * parts. Might be empty, but not null.
     * @return The list of the parts
     */
    List<Part> getParts();

    /**
     * Returns the size of the whole multipart request.
     * @return The full size of the request
     */
    long getSize();

    /**
     * Returns the headers of the HTTP request, provided
     * by the Jax-Rs runtime.
     * @return The map of the headers
     */
    MultivaluedMap<String, String> getHeaders();

}