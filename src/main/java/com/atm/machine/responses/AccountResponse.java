package com.atm.machine.responses;

import com.atm.machine.models.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(value = { "systemId", "version", "status", "dateCreated", "dateUpdated", "notes", "pin" })
public class AccountResponse extends Account {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7013429260243542172L;
	@Getter @Setter
	private double maximumWithdrawalAmount;
	@Getter @Setter
	private String responseCode;
	@Getter @Setter
	private String responseStatus;
	@Getter @Setter
	private String responseMessage;

	
}
