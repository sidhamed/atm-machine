package com.atm.machine.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.atm.machine.requests.AccountRequest;
import com.atm.machine.responses.AccountResponse;
import com.atm.machine.validation.BalanceValidationUtil;

@ExtendWith(MockitoExtension.class)
class BalanceValidationUtilTest {

	private static final String FAILED = "failed";
	@InjectMocks
	private BalanceValidationUtil balanceValidationUtil;

	@Test
	@DisplayName("Test return accurate response")
	void shouldReturnAccurateResponse() {

		AccountResponse response = new AccountResponse();
		balanceValidationUtil.response(response, "a", "b", "c");
		assertThat(response.getResponseCode()).isEqualTo("a");
		assertThat(response.getResponseMessage()).isEqualTo("b");
		assertThat(response.getResponseStatus()).isEqualTo("c");

	}

	@Test
	@DisplayName("Test return successful response")
	void shouldReturnSuccessfulResponse() {

		AccountResponse response = new AccountResponse();
		AccountRequest request = new AccountRequest();
		balanceValidationUtil.successfulResponse(request, response);
		assertThat(response.getResponseCode()).isEqualTo("0");
		assertThat(response.getResponseMessage()).isEqualTo("successful");
		assertThat(response.getResponseStatus()).isEqualTo("approved");

	}
	
	@Test
	@DisplayName("Test return successful response from validate")
	void shouldReturnSuccessfulResponseFromValidate() {

		AccountResponse response = new AccountResponse();
		AccountRequest request = new AccountRequest();
		request.setAccountNumber("987");
		request.setPin("987");
		response = balanceValidationUtil.validate(request);
		assertThat(response.getResponseCode()).isEqualTo("0");
		assertThat(response.getResponseMessage()).isEqualTo("successful");
		assertThat(response.getResponseStatus()).isEqualTo("approved");

	}
	
	@Test
	@DisplayName("Test return invalid request response")
	void shouldReturnInvalidRequestResponse() {

		AccountResponse response = new AccountResponse();
		balanceValidationUtil.invalidRequestResponse(response);
		assertThat(response.getResponseCode()).isEqualTo("10");
		assertThat(response.getResponseMessage()).isEqualTo("Invalid request");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}
	
	@Test
	@DisplayName("Test return invalid request response in validate")
	void shouldReturnInvalidRequestResponse_V() {

		AccountResponse response = new AccountResponse();
		AccountRequest request = null;
		response = balanceValidationUtil.validate(request);
		assertThat(response.getResponseCode()).isEqualTo("10");
		assertThat(response.getResponseMessage()).isEqualTo("Invalid request");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}
	
	@Test
	@DisplayName("Test return invalid account response")
	void shouldReturnInvalidAccountResponse() {

		AccountResponse response = new AccountResponse();
		AccountRequest request = new AccountRequest();

		balanceValidationUtil.invalidAccountResponse(request, response);
		assertThat(response.getResponseCode()).isEqualTo("8");
		assertThat(response.getResponseMessage()).isEqualTo("Account Number is mandatory");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}
	
	@Test
	@DisplayName("Test return invalid account response from validate when account number is null")
	void shouldReturnInvalidAccountResponse_V() {

		AccountResponse response = new AccountResponse();
		AccountRequest request = new AccountRequest();

		response = balanceValidationUtil.validate(request);
		assertThat(response.getResponseCode()).isEqualTo("8");
		assertThat(response.getResponseMessage()).isEqualTo("Account Number is mandatory");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}
	
	@Test
	@DisplayName("Test return invalid account response from validate when account number is empty")
	void shouldReturnInvalidAccountResponseWhenEmpty() {

		AccountResponse response = new AccountResponse();
		AccountRequest request = new AccountRequest();
		request.setAccountNumber("");

		response =balanceValidationUtil.validate(request);
		assertThat(response.getResponseCode()).isEqualTo("8");
		assertThat(response.getResponseMessage()).isEqualTo("Account Number is mandatory");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}
	
	@Test
	@DisplayName("Test return invalid PIN response from validate when PIN number is null")
	void shouldReturnInvalidPINResponseWhenNull() {

		AccountResponse response = new AccountResponse();
		AccountRequest request = new AccountRequest();
		request.setAccountNumber("987"); // this needs to pass first
		response = balanceValidationUtil.validate(request);
		assertThat(response.getResponseCode()).isEqualTo("9");
		assertThat(response.getResponseMessage()).isEqualTo("Pin is mandatory");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}
	
	@Test
	@DisplayName("Test return invalid PIN response from validate when PIN is empty")
	void shouldReturnInvalidPINResponseWhenEmpty() {

		AccountResponse response = new AccountResponse();
		AccountRequest request = new AccountRequest();
		request.setPin("");
		request.setAccountNumber("987"); // this needs to pass first
		response = balanceValidationUtil.validate(request);
		assertThat(response.getResponseCode()).isEqualTo("9");
		assertThat(response.getResponseMessage()).isEqualTo("Pin is mandatory");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}
	
	@Test
	@DisplayName("Test return invalid PIN response")
	void shouldReturnInvalidPINResponse() {

		AccountResponse response = new AccountResponse();
		AccountRequest request = new AccountRequest();

		balanceValidationUtil.pinIsRequiredResponse(request, response);
		assertThat(response.getResponseCode()).isEqualTo("9");
		assertThat(response.getResponseMessage()).isEqualTo("Pin is mandatory");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}

}
