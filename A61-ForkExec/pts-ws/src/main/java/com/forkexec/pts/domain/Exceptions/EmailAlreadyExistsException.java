package com.forkexec.pts.domain.Exceptions;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException() {
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
