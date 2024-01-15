package com.codej.base.exception;

public class CUserExistException extends RuntimeException {
    public CUserExistException(String message, Throwable t) {
        super(message, t);
    }

    public CUserExistException(String message) {
        super(message);
    }

    public CUserExistException() {
        super();
    }
}
