package com.codej.base.exception;

public class CUserExistException extends CBaseException {
    public CUserExistException() {
        super("existingUser");
    }

    public CUserExistException(String detail) {
        super("existingUser", detail);
    }
}
