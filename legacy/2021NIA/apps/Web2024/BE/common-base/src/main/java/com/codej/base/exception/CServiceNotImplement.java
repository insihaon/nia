package com.codej.base.exception;

public class CServiceNotImplement extends CBaseException {
    public CServiceNotImplement() {
        super("serviceNotImplement");
    }

    public CServiceNotImplement(String detail) {
        super("serviceNotImplement", detail);
    }
}
