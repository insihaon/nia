package com.codej.base.exception;

public class CNotOwnerException extends CBaseException {
	public CNotOwnerException() {
        super("notOwner");
    }

    public CNotOwnerException(String detail) {
        super("notOwner", detail);
    }
}
