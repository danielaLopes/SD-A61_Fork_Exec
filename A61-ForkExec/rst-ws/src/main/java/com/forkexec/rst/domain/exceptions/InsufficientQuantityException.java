package com.forkexec.rst.domain.exceptions;

public class InsufficientQuantityException extends Exception{
    public InsufficientQuantityException() {
    }

    public InsufficientQuantityException(String message) {
        super(message);
    }
}