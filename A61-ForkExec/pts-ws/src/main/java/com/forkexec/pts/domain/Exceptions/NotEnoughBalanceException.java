package com.forkexec.pts.domain.Exceptions;

public class NotEnoughBalanceException extends Exception {

    public NotEnoughBalanceException() {
    }

    public NotEnoughBalanceException(String message) {
        super(message);
    }
}
