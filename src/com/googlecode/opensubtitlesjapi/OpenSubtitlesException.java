package com.googlecode.opensubtitlesjapi;

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
