package com.atm.machine.exceptions;

import com.atm.machine.models.Account;

public class AmountNotAccessibleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381895799350615142L;
	private final Account account;

	public Account getAccount() {
		return account;
	}

	

	public AmountNotAccessibleException() {
		super();
		this.account = new Account();
		
	}

	public AmountNotAccessibleException(Account account) {
		super();
		
		this.account = account;
	}

	public AmountNotAccessibleException(Account account , String message) {
		super(message);
		this.account = account;
		
	}

	public AmountNotAccessibleException(Account account ,Throwable cause) {
		super(cause);
		this.account = account;
		
	}

	public AmountNotAccessibleException(Account account ,String message, Throwable cause) {
		super(message, cause);
		this.account = account;
	}
}
