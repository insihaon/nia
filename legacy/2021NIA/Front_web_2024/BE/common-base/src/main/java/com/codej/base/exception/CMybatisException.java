package com.codej.base.exception;

public class CMybatisException extends CBaseException {
    public CMybatisException() {
        super("mybatisException");
    }

    public CMybatisException(String detail) {
        super("mybatisException", detail);
    }
}
