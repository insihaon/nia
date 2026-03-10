package com.codej.base.exception;

public class CHttpRelayServiceFail extends CBaseException {
    public CHttpRelayServiceFail() {
        super("httpRelayServiceFail");
    }

    public CHttpRelayServiceFail(String detail) {
        super("httpRelayServiceFail", detail);
    }
}
