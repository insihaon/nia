package com.codej.base.exception;

import lombok.Data;

@Data
public class CUntrustedRequestException extends CBaseException {
    public CUntrustedRequestException() {
        super("untrustedRequest");
    }

    public CUntrustedRequestException(String detail) {
        super("unKnown", detail);
    }
}

    
