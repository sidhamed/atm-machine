package com.atm.machine.exceptions;

public class WrongPINException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381895799350615142L;

	public WrongPINException() {
		super();
	}

	public WrongPINException(String message) {
		super(message);
	}

	public WrongPINException(Throwable cause) {
		super(cause);
	}

	public WrongPINException(String message, Throwable cause) {
		super(message, cause);
	}
}
