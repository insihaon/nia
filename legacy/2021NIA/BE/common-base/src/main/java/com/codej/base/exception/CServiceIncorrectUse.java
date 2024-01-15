package com.codej.base.exception;

public class CServiceIncorrectUse extends RuntimeException {
    public CServiceIncorrectUse(String message, Throwable t) {
        super(message, t);
    }

    public CServiceIncorrectUse(String message) {
        super(message);
    }

    public CServiceIncorrectUse() {
        super();
    }
}
