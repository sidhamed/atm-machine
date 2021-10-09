package com.atm.machine.requests;

import lombok.Getter;
import lombok.Setter;


public class WithdrawalRequest {

	@Getter
	@Setter
	private String accountNumber;
	@Getter
	@Setter
	private String pin;
	@Getter
	@Setter
	private double amount;

}
