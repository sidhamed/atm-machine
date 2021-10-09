package com.atm.machine.validation;

import org.springframework.stereotype.Component;

import com.atm.machine.requests.WithdrawalRequest;
import com.atm.machine.responses.WithdrawalResponse;

@Component
public class WithdrawalValidationUtil {

	private static final String FAILED = "failed";

	public WithdrawalResponse validate(WithdrawalRequest request) {
		WithdrawalResponse validationResponse = new WithdrawalResponse();
		if (nullRequest(request)) {
			validationResponse.setAccountNumber("");
			validationResponse.setResponseCode("10");
			validationResponse.setResponseMessage("Invalid request");
			validationResponse.setResponseStatus(FAILED);
			return validationResponse;
		}
		if (invalidAccountNumber(request)) {
			validationResponse.setAccountNumber("");
			validationResponse.setResponseCode("8");
			validationResponse.setResponseMessage("Account Number is mandatory");
			validationResponse.setResponseStatus(FAILED);
			return validationResponse;
		}
		request.setAccountNumber(request.getAccountNumber().trim());

		if (invalidPin(request)) {
			validationResponse.setAccountNumber(request.getAccountNumber());
			validationResponse.setResponseCode("9");
			validationResponse.setResponseMessage("PIN is mandatory");
			validationResponse.setResponseStatus(FAILED);
			return validationResponse;
		}
		request.setPin(request.getPin().trim());

		if (amountLessThanOrEqualZero(request)) {

			validationResponse.setAccountNumber(request.getAccountNumber());
			validationResponse.setResponseCode("11");
			validationResponse.setAmount(request.getAmount());
			validationResponse.setResponseMessage("Invalid withdrawal amount");
			validationResponse.setResponseStatus(FAILED);
			return validationResponse;

		}

		validationResponse.setAccountNumber(request.getAccountNumber());
		validationResponse.setAmount(request.getAmount());
		validationResponse.setResponseCode("0");
		validationResponse.setResponseMessage("successful validation");
		validationResponse.setResponseStatus("approved");
		return validationResponse;
	}

	public boolean amountLessThanOrEqualZero(WithdrawalRequest request) {
		return request.getAmount() <= 0;
	}

	public boolean invalidPin(WithdrawalRequest request) {
		return request.getPin() == null || request.getPin().equalsIgnoreCase("");
	}

	public boolean invalidAccountNumber(WithdrawalRequest request) {
		return request.getAccountNumber() == null || request.getAccountNumber().equalsIgnoreCase("");
	}

	public boolean nullRequest(WithdrawalRequest request) {
		return request == null;
	}

	public boolean validationNotSuccessful(WithdrawalResponse response) {
		return !response.getResponseCode().equalsIgnoreCase("0");
	}

}
