package com.codej.base.exception;

public class CUnknowException extends RuntimeException {
    
    public CUnknowException(String message, Throwable t) {
        super(message, t);
    }

    public CUnknowException(String message) {
        super(message);
    }

    public CUnknowException() {
        super();
    }
}
