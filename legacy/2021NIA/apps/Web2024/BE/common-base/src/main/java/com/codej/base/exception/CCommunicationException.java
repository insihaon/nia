package com.codej.base.exception;

public class CCommunicationException extends CBaseException {
    public CCommunicationException() {
        super("communicationError");
    }

    public CCommunicationException(String detail) {
        super("communicationError", detail);
    }
}
