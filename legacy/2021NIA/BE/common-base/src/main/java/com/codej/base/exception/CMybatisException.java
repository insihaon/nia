package com.codej.base.exception;

public class CMybatisException extends RuntimeException {
    
    public CMybatisException(String message, Throwable t) {
        super(message, t);
    }

    public CMybatisException(String message) {
        super(message);
    }

    public CMybatisException() {
        super();
    }
}
