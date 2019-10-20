package com.forkexec.rst.domain.exceptions;

public class BadQuantityException extends Exception{
    public BadQuantityException() {
    }

    public BadQuantityException(String message) {
        super(message);
    }
}
