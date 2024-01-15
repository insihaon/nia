package com.codej.base.exception;

public class CNotOwnerException extends RuntimeException {

	private static final long serialVersionUID = 2241549550934267615L;

	public CNotOwnerException(String message, Throwable t) {
		super(message, t);
	}

	public CNotOwnerException(String message) {
		super(message);
	}

	public CNotOwnerException() {
		super();
	}
}
