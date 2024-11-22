package com.codej.base.exception;

public class CServiceException extends CBaseException {
    public CServiceException() {
        super("serviceException");
    }

    public CServiceException(String detail) {
        super("serviceException", detail);
    }
}
