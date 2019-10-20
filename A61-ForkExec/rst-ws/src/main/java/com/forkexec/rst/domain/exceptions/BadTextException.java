package com.forkexec.rst.domain.exceptions;

public class BadTextException extends Exception{
    public BadTextException() {
    }

    public BadTextException(String message) {
        super(message);
    }
}
