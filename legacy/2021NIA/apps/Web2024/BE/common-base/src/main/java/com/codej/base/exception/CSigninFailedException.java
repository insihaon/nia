package com.codej.base.exception;

public class CSigninFailedException extends CBaseException {
    public CSigninFailedException() {
        super("emailSigninFailed");
    }

    public CSigninFailedException(String detail) {
        super("emailSigninFailed", detail);
    }
}
