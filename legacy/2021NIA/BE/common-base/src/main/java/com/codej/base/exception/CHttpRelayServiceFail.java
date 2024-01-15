package com.codej.base.exception;

import lombok.Getter;

@Getter
public class CHttpRelayServiceFail extends RuntimeException {

    private String detailMessage;

    public CHttpRelayServiceFail(String message, Throwable t) {
        super(message, t);
    }

    public CHttpRelayServiceFail(String message) {
        super(message);
    }

    public CHttpRelayServiceFail() {
        super();
    }

    public CHttpRelayServiceFail(Exception e) {
        super();
        this.detailMessage = e.toString();
    }
}
