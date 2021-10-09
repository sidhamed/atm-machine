package com.atm.machine.models;

public class DispenseResult {
	
	private double amountAfterDispense;
	private BankNotes dispensedNotesSoFar;
	
	public DispenseResult(double amountAfterDispense, BankNotes dispensedNotesSoFar) {
		super();
		this.amountAfterDispense = amountAfterDispense;
		this.dispensedNotesSoFar = dispensedNotesSoFar;
	}

	public double getAmountAfterDispense() {
		return amountAfterDispense;
	}

	public void setAmountAfterDispense(double amountAfterDispense) {
		this.amountAfterDispense = amountAfterDispense;
	}

	public BankNotes getDispensedNotesSoFar() {
		return dispensedNotesSoFar;
	}

	public void setDispensedNotesSoFar(BankNotes dispensedNotesSoFar) {
		this.dispensedNotesSoFar = dispensedNotesSoFar;
	}
	
	

}
