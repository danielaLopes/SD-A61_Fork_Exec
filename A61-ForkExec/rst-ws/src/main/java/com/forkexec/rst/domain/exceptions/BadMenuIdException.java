package com.forkexec.rst.domain.exceptions;

public class BadMenuIdException extends Exception{
    public BadMenuIdException() {
    }

    public BadMenuIdException(String message) {
        super(message);
    }
}
