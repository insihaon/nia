package com.codej.base.exception;

public class CUserNotFoundException extends CBaseException {
    public CUserNotFoundException() {
        super("userNotFound");
    }

    public CUserNotFoundException(String detail) {
        super("userNotFound", detail);
    }
}
