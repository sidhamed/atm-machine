package com.atm.machine.validation;

import org.springframework.stereotype.Component;

import com.atm.machine.requests.AccountRequest;
import com.atm.machine.responses.AccountResponse;

@Component
public class BalanceValidationUtil {

	private static final String FAILED = "failed";

	public AccountResponse validate(AccountRequest request) {
		
		AccountResponse validationResponse = new AccountResponse();
		// trim string values
		if (request == null) {
			invalidRequestResponse(validationResponse);
			return validationResponse;
		}

		if (request.getAccountNumber() == null) {
			invalidAccountResponse(request, validationResponse);
			return validationResponse;
		}
		request.setAccountNumber(request.getAccountNumber().trim());

		if (request.getAccountNumber().equalsIgnoreCase("")) {

			invalidAccountResponse(request, validationResponse);
			return validationResponse;
		}

		if (request.getPin() == null) {
			pinIsRequiredResponse(request, validationResponse);
			return validationResponse;
		}
		request.setPin(request.getPin().trim());

		if (request.getPin().equalsIgnoreCase("")) {

			pinIsRequiredResponse(request, validationResponse);
			return validationResponse;
		}

		successfulResponse(request, validationResponse);
		return validationResponse;
	}

	public void successfulResponse(AccountRequest request, AccountResponse validationResponse) {
		validationResponse.setAccountNumber(request.getAccountNumber());
		response(validationResponse, "0", "successful", "approved");

	}

	public void invalidRequestResponse(AccountResponse validationResponse) {
		validationResponse.setAccountNumber("");
		response(validationResponse, "10", "Invalid request", FAILED);

	}

	public void pinIsRequiredResponse(AccountRequest request, AccountResponse validationResponse) {
		validationResponse.setAccountNumber(request.getAccountNumber());
		response(validationResponse, "9", "Pin is mandatory", FAILED);

	}

	public void invalidAccountResponse(AccountRequest request, AccountResponse validationResponse) {
		validationResponse.setAccountNumber(request.getAccountNumber());
		response(validationResponse, "8", "Account Number is mandatory", FAILED);
	}

	public void response(AccountResponse validationResponse, String code, String msg, String status) {
		validationResponse.setResponseCode(code);
		validationResponse.setResponseMessage(msg);
		validationResponse.setResponseStatus(status);
	}

	public boolean validationNotSuccessful(AccountResponse response) {
		return !response.getResponseCode().equalsIgnoreCase("0");
	}

}
