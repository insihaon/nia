package com.codej.base.exception;

public class CAuthenticationException extends RuntimeException {
    public CAuthenticationException(String message, Throwable t) {
        super(message, t);
    }

    public CAuthenticationException(String message) {
        super(message);
    }

    public CAuthenticationException() {
        super();
    }
}
