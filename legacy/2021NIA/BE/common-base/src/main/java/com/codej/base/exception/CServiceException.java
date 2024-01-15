package com.codej.base.exception;

public class CServiceException extends RuntimeException {
    public CServiceException(String message, Throwable t) {
        super(message, t);
    }

    public CServiceException(String message) {
        super(message);
    }

    public CServiceException() {
        super();
    }
}
