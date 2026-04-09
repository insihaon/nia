package com.codej.base.exception;

public class CUnknowException extends CBaseException {
    public CUnknowException() {
        super("unKnown");
    }

    public CUnknowException(String detail) {
        super("unKnown", detail);
    }
}
