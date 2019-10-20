package com.forkexec.hub.domain.exceptions;

public class InvalidUserId_Exception extends Exception {

    /**
     *
     * @param message
     */
    public InvalidUserId_Exception(String message) {
        super(message);
    }

    /**
     *
     * @param cause
     * @param message
     */
    public InvalidUserId_Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
