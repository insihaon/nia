package com.codej.base.exception;

public class CForbiddenRequestException extends CBaseException {
    public CForbiddenRequestException() {
        super("forbiddenRequest");
    }

    public CForbiddenRequestException(String detail) {
        super("forbiddenRequest", detail);
    }
}
