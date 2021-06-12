package com.atm.machine.validation;

import org.springframework.stereotype.Component;

import com.atm.machine.requests.AccountRequest;
import com.atm.machine.requests.WithdrawalRequest;
import com.atm.machine.responses.AccountResponse;
import com.atm.machine.responses.WithdrawalResponse;

@Component
public class WithdrawalValidationUtil {

	public WithdrawalResponse validate(WithdrawalRequest request) {
		WithdrawalResponse validationResponse = new WithdrawalResponse();
		if (request != null) {
			if (request.getAccountNumber() != null) {
				request.setAccountNumber(request.getAccountNumber().trim());

				if (request.getAccountNumber().equalsIgnoreCase("")) {

					validationResponse.setAccountNumber(request.getAccountNumber());
					validationResponse.setResponseCode("8");
					validationResponse.setResponseMessage("Account Number is mandatory");
					validationResponse.setResponseStatus("failed");
					return validationResponse;
				}

			} else {

				validationResponse.setAccountNumber("");
				validationResponse.setResponseCode("8");
				validationResponse.setResponseMessage("Account Number is mandatory");
				validationResponse.setResponseStatus("failed");
				return validationResponse;

			}
			if (request.getPin() != null) {
				request.setPin(request.getPin().trim());

				if (request.getPin().equalsIgnoreCase("")) {

					validationResponse.setAccountNumber(request.getAccountNumber());
					validationResponse.setResponseCode("9");
					validationResponse.setResponseMessage("PIN is mandatory");
					validationResponse.setResponseStatus("failed");
					return validationResponse;
				}

			} else {
				validationResponse.setAccountNumber(request.getAccountNumber());
				validationResponse.setResponseCode("9");
				validationResponse.setResponseMessage("PIN is mandatory");
				validationResponse.setResponseStatus("failed");
				return validationResponse;
			}

			if (request.getAmount() <= 0) {

				validationResponse.setAccountNumber(request.getAccountNumber());
				validationResponse.setResponseCode("11");
				validationResponse.setAmount(request.getAmount());
				validationResponse.setResponseMessage("Invalid withdrawal amount");
				validationResponse.setResponseStatus("failed");
				return validationResponse;

			} 

		} else {
			validationResponse.setAccountNumber("");
			validationResponse.setResponseCode("10");
			validationResponse.setResponseMessage("Invalid request");
			validationResponse.setResponseStatus("failed");
			return validationResponse;
		}

		validationResponse.setAccountNumber(request.getAccountNumber());
		validationResponse.setAmount(request.getAmount());
		validationResponse.setResponseCode("0");
		validationResponse.setResponseMessage("successful validation");
		validationResponse.setResponseStatus("approved");
		return validationResponse;
	}

}
