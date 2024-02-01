package com.codej.base.exception;

public class CForbiddenRequestException extends RuntimeException {

    public CForbiddenRequestException(String message, Throwable t) {
        super(message, t);
    }

    public CForbiddenRequestException(String message) {
        super(message);
    }

    public CForbiddenRequestException() {
        super();
    }
}
