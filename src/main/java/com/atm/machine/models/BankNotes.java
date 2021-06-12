package com.atm.machine.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "bankNotes")
@JsonIgnoreProperties(value = { "systemId", "version", "status", "dateCreated", "dateUpdated", "notes" , "responseCode" , "responseStatus" , "responseMessage" })

public class BankNotes extends CommonModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1851690181119300225L;
	private int fifty;
	private int twenty;
	private int ten;
	private int five;

	@Transient
	private String responseCode;
	@Transient
	private String responseStatus;
	@Transient
	private String responseMessage;

	
	public int getFifty() {
		return fifty;
	}

	public void setFifty(int fifty) {
		this.fifty = fifty;
	}

	public int getTwenty() {
		return twenty;
	}

	public void setTwenty(int twenty) {
		this.twenty = twenty;
	}

	public int getTen() {
		return ten;
	}

	public void setTen(int ten) {
		this.ten = ten;
	}

	public int getFive() {
		return five;
	}

	public void setFive(int five) {
		this.five = five;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + fifty;
		result = prime * result + five;
		result = prime * result + ((responseCode == null) ? 0 : responseCode.hashCode());
		result = prime * result + ((responseMessage == null) ? 0 : responseMessage.hashCode());
		result = prime * result + ((responseStatus == null) ? 0 : responseStatus.hashCode());
		result = prime * result + ten;
		result = prime * result + twenty;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankNotes other = (BankNotes) obj;
		if (fifty != other.fifty)
			return false;
		if (five != other.five)
			return false;
		if (responseCode == null) {
			if (other.responseCode != null)
				return false;
		} else if (!responseCode.equals(other.responseCode))
			return false;
		if (responseMessage == null) {
			if (other.responseMessage != null)
				return false;
		} else if (!responseMessage.equals(other.responseMessage))
			return false;
		if (responseStatus == null) {
			if (other.responseStatus != null)
				return false;
		} else if (!responseStatus.equals(other.responseStatus))
			return false;
		if (ten != other.ten)
			return false;
		if (twenty != other.twenty)
			return false;
		return true;
	}
	
	

	@Override
	public String toString() {
		return "BankNotes [fifty=" + fifty + ", twenty=" + twenty + ", ten=" + ten + ", five=" + five
				+ ", responseCode=" + responseCode + ", responseStatus=" + responseStatus + ", responseMessage="
				+ responseMessage + "]";
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void clean() {
		// TODO Auto-generated method stub

	}

}
