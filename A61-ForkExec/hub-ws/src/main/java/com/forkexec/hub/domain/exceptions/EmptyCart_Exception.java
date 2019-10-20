package com.forkexec.hub.domain.exceptions;

public class EmptyCart_Exception extends Exception {

    /**
     *
     * @param message
     */
    public EmptyCart_Exception(String message) {
        super(message);
    }

    /**
     *
     * @param cause
     * @param message
     */
    public EmptyCart_Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
