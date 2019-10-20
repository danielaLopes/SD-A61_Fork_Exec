package com.forkexec.rst.domain.exceptions;

public class BadInitException extends Exception{
    public BadInitException() {
    }

    public BadInitException(String message) {
        super(message);
    }

    //public BadInitException(String message, String faultInfo) {
      //  super(message, faultInfo);
    //}
}
