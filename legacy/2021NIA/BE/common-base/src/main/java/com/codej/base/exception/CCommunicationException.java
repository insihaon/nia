package com.codej.base.exception;

public class CCommunicationException extends RuntimeException {
    public CCommunicationException(String message, Throwable t) {
        super(message, t);
    }

    public CCommunicationException(String message) {
        super(message);
    }

    public CCommunicationException() {
        super();
    }
}
