package com.codej.base.exception;

public class CAuthenticationException extends CBaseException {
    public CAuthenticationException() {
        super("entryPointException");
    }

    public CAuthenticationException(String detail) {
        super("entryPointException", detail);
    }
}
