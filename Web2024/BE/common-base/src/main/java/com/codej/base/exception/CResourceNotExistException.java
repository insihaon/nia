package com.codej.base.exception;

public class CResourceNotExistException extends RuntimeException {
    public CResourceNotExistException(String message, Throwable t) {
        super(message, t);
    }

    public CResourceNotExistException(String message) {
        super(message);
    }

    public CResourceNotExistException() {
        super();
    }
}
