package com.atm.machine.exceptions;

public class ATMInitialisationException extends CommonServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381895799350615142L;
	


	public ATMInitialisationException() {
		super();
	

	}

	public ATMInitialisationException(String message) {
		super(message);
		
	}

	public ATMInitialisationException(Throwable cause) {
		super(cause);
		

	}

	public ATMInitialisationException(String message, Throwable cause) {
		super(message, cause);
		
		
	}
}
