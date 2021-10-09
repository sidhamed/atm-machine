package com.atm.machine.services;

import com.atm.machine.models.BankNotes;

import lombok.Getter;
import lombok.Setter;

public class DispenseOrder {
	@Getter
	@Setter
	private double amount;
	@Getter
	@Setter
	private BankNotes notes;
	@Getter
	@Setter
	private BankNotes notesToBeDispensed;
	@Getter
	@Setter
	private int[] dispensibleNotes;
	@Getter
	@Setter
	private int currentBill;

	public DispenseOrder(double amount, BankNotes notes, BankNotes notesToBeDispensed, int[] dispensibleNotes,
			int currentBill) {
		this.amount = amount;
		this.notes = notes;
		this.notesToBeDispensed = notesToBeDispensed;
		this.dispensibleNotes = dispensibleNotes;
		this.currentBill = currentBill;
	}

	public DispenseOrder() {
		super();
	}
	
	
	
}