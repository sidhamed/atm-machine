package com.atm.machine.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bankNotes")
@JsonIgnoreProperties(value = { "systemId", "version", "status", "dateCreated", "dateUpdated", "notes", "responseCode",
		"responseStatus", "responseMessage" })

public class BankNotes extends CommonModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1851690181119300225L;
	@Getter
	@Setter
	private int fifty;
	@Getter
	@Setter
	private int twenty;
	@Getter
	@Setter
	private int ten;
	@Getter
	@Setter
	private int five;

	@Transient
	@Getter
	@Setter
	private String responseCode;
	@Transient
	@Getter
	@Setter
	private String responseStatus;
	@Transient
	@Getter
	@Setter
	private String responseMessage;

	
}
