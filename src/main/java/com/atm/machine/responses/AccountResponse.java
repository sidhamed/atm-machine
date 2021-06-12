package com.atm.machine.responses;

import com.atm.machine.models.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "systemId", "version", "status", "dateCreated", "dateUpdated", "notes", "pin" })
public class AccountResponse extends Account {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7013429260243542172L;
	private double maximumWithdrawalAmount;
	private String responseCode;
	private String responseStatus;
	private String responseMessage;

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

	public double getMaximumWithdrawalAmount() {
		return maximumWithdrawalAmount;
	}

	public void setMaximumWithdrawalAmount(double maximumWithdrawalAmount) {
		this.maximumWithdrawalAmount = maximumWithdrawalAmount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(maximumWithdrawalAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((responseCode == null) ? 0 : responseCode.hashCode());
		result = prime * result + ((responseMessage == null) ? 0 : responseMessage.hashCode());
		result = prime * result + ((responseStatus == null) ? 0 : responseStatus.hashCode());
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
		AccountResponse other = (AccountResponse) obj;
		if (Double.doubleToLongBits(maximumWithdrawalAmount) != Double.doubleToLongBits(other.maximumWithdrawalAmount))
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
		return true;
	}

	@Override
	public String toString() {
		return "AccountResponse [maximumWithdrawalAmount=" + maximumWithdrawalAmount + ", responseCode=" + responseCode
				+ ", responseStatus=" + responseStatus + ", responseMessage=" + responseMessage + ", accountNumber="
				+ accountNumber + ", balance=" + balance + ", overdraft=" + overdraft + ", systemId=" + systemId
				+ ", dateCreated=" + dateCreated + ", dateUpdated=" + dateUpdated + "]";
	}

}
