package com.forkexec.pts.ws.cli;

/**
 *
 * Exception to be thrown when something is wrong with the front end.
 *
 */

public class PointsFrontEndException extends Exception {

    /*private static final long serialVersionUID = 1L;*/

    public PointsFrontEndException() {
        super();
    }

    public PointsFrontEndException(String message) {
        super(message);
    }

    public PointsFrontEndException(Throwable cause) {
        super(cause);
    }

    public PointsFrontEndException(String message, Throwable cause) {
        super(message, cause);
    }
}
