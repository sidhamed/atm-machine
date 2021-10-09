package com.atm.machine.exceptions;

import com.atm.machine.models.Account;
import com.atm.machine.models.BankNotes;

public class IndispensibleAmountException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7381895799350615142L;
	private final Account account;
	private final BankNotes bankNotes;

	public BankNotes getBankNotes() {
		return bankNotes;
	}

	public Account getAccount() {
		return account;
	}

	public IndispensibleAmountException() {
		super();
		this.account = new Account();
		this.bankNotes = new BankNotes();

	}

	public IndispensibleAmountException(Account account, BankNotes bankNotes, String message) {
		super(message);
		this.account = account;
		this.bankNotes = bankNotes;
	}

	public IndispensibleAmountException(Throwable cause) {
		super(cause);
		this.account = new Account();
		this.bankNotes = new BankNotes();

	}

	public IndispensibleAmountException(String message, Throwable cause) {
		super(message, cause);
		this.account = new Account();
		this.bankNotes = new BankNotes();
		
	}
}
