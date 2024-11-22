package com.codej.base.exception;

public class CResourceNotExistException extends CBaseException {
    public CResourceNotExistException() {
        super("resourceNotExist");
    }

    public CResourceNotExistException(String detail) {
        super("resourceNotExist", detail);
    }
}
