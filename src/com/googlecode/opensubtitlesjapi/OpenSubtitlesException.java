/*
 *  Copyright 2011 daniele.belletti@gmail.com.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.googlecode.opensubtitlesjapi;

/**
 *
 * @author daniele.belletti@gmail.com
 */
public class OpenSubtitlesException extends Exception {

    /**
     * Creates a new instance of <code>OpenSubtitlesException</code> without detail message.
     */
    public OpenSubtitlesException() {
      super();
    }


    /**
     * Constructs an instance of <code>OpenSubtitlesException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public OpenSubtitlesException(String msg) {
        super(msg);
    }

     public OpenSubtitlesException(String msg, Throwable throwable) {
        super(msg,throwable);
    }
}
