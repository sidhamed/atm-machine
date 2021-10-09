package com.atm.machine.models;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account")
public class Account extends CommonModel {

	/**
	 * 
	 */
	protected static final long serialVersionUID = 2438772195091643043L;
	@Getter
	@Setter
	protected String accountNumber;
	@Getter
	@Setter
	protected String pin;
	@Getter
	@Setter
	protected double balance;
	@Getter
	@Setter
	protected double overdraft;
	@Getter
	@Setter
	protected String currency;


}
