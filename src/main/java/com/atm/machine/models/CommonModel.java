package com.atm.machine.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

/**
 * This class is base model that must be extended by every model in the project;
 * it contains the frequently used fields by the project beans.
 * 
 * @author Siddiq Hamed
 * 
 * 
 */
@MappedSuperclass

public abstract class CommonModel implements Serializable {

	private static final long serialVersionUID = 7238055550254241600L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	@Setter
	private Long systemId;
	@Version
	@Column(name = "version")
	@Getter
	@Setter
	private int version;
	@Getter
	@Setter
	private String status;
	@Getter
	@Setter
	private String notes;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Getter
	@Setter
	private Date dateCreated;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Getter
	@Setter
	private Date dateUpdated;

	protected CommonModel() {
		super();
	}

	@PrePersist
	protected void onCreate() {
		dateCreated = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		dateUpdated = new Date();
	}

}
