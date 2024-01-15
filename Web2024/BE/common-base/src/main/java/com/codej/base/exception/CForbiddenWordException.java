package com.codej.base.exception;

public class CForbiddenWordException extends RuntimeException {

    public CForbiddenWordException(String message, Throwable t) {
        super(message, t);
    }

    public CForbiddenWordException(String message) {
        super(message);
    }

    public CForbiddenWordException() {
        super();
    }
}
