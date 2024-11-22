package com.codej.base.exception;

public class CServiceIncorrectUse extends CBaseException {
    public CServiceIncorrectUse() {
        super("serviceIncorrectUse");
    }

    public CServiceIncorrectUse(String detail) {
        super("serviceIncorrectUse", detail);
    }
}
