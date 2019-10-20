package com.forkexec.pts.domain.Exceptions;

public class BadInitException extends Exception{
    public BadInitException() {
    }

    public BadInitException(String message) {
        super(message);
    }
}
