package com.atm.machine.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.atm.machine.requests.WithdrawalRequest;
import com.atm.machine.responses.WithdrawalResponse;
import com.atm.machine.validation.WithdrawalValidationUtil;

@ExtendWith(MockitoExtension.class)
class WithdrawalValidationUtilTest {

	private static final String FAILED = "failed";
	@InjectMocks
	private WithdrawalValidationUtil withdrawalValidationUtil;

	@Test
	@DisplayName("Test return true when request is null")
	void shouldReturnTrueOnNullRequest() {

		WithdrawalRequest request = null;

		assertThat(withdrawalValidationUtil.nullRequest(request)).isTrue();

	}

	@Test
	@DisplayName("Test return false when request is not null")
	void shouldReturnFalseOnNonNullRequest() {

		WithdrawalRequest request = new WithdrawalRequest();

		assertThat(withdrawalValidationUtil.nullRequest(request)).isFalse();

	}

	@Test
	@DisplayName("Test return true when account is null")
	void shouldReturnTrueOnNullAccountNumber() {

		WithdrawalRequest request = new WithdrawalRequest();

		assertThat(withdrawalValidationUtil.invalidAccountNumber(request)).isTrue();

	}

	@Test
	@DisplayName("Test return false when account is not null")
	void shouldReturnFalseOnNotNullAccountNumber() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("accountNotNullS");
		assertThat(withdrawalValidationUtil.invalidAccountNumber(request)).isFalse();

	}

	@Test
	@DisplayName("Test return true when account is empty")
	void shouldReturnTrueOnEmptyAccountNumber() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("");

		assertThat(withdrawalValidationUtil.invalidAccountNumber(request)).isTrue();

	}

	@Test
	@DisplayName("Test return false when account is not empty")
	void shouldReturnFalseOnNotEmptyAccountNumber() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("987987987");

		assertThat(withdrawalValidationUtil.invalidAccountNumber(request)).isFalse();

	}

	@Test
	@DisplayName("Test return true when pin is null")
	void shouldReturnTrueOnNullPin() {

		WithdrawalRequest request = new WithdrawalRequest();

		assertThat(withdrawalValidationUtil.invalidPin(request)).isTrue();

	}

	@Test
	@DisplayName("Test return false when pin is not null")
	void shouldReturnFalseOnNotNullPin() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setPin("pinNotNull");
		assertThat(withdrawalValidationUtil.invalidPin(request)).isFalse();

	}

	@Test
	@DisplayName("Test return true when pin is empty")
	void shouldReturnTrueOnEmptyPin() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setPin("");

		assertThat(withdrawalValidationUtil.invalidPin(request)).isTrue();

	}

	@Test
	@DisplayName("Test return false when pin is not empty")
	void shouldReturnFalseOnNotEmptyPin() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setPin("0000");

		assertThat(withdrawalValidationUtil.invalidPin(request)).isFalse();

	}

	@Test
	@DisplayName("Test return true when account is equal or less than zero")
	void shouldReturnTrueWhenLessThanOrEqualZero() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAmount(-5);

		assertThat(withdrawalValidationUtil.amountLessThanOrEqualZero(request)).isTrue();

	}

	@Test
	@DisplayName("Test return true when account is equal or less than zero")
	void shouldReturnFalseWhenGreaterThanZero() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAmount(5);

		assertThat(withdrawalValidationUtil.amountLessThanOrEqualZero(request)).isFalse();

	}

	@Test
	@DisplayName("Test return invalid request response when request is null")
	void shouldReturnInvalidRequestResponseOnNullRequest() {

		WithdrawalRequest request = null;
		WithdrawalResponse response = withdrawalValidationUtil.validate(request);
		assertThat(response.getResponseCode()).isEqualTo("10");
		assertThat(response.getResponseMessage()).isEqualTo("Invalid request");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}

	@Test
	@DisplayName("Test return invalid account number response when account is null or empty")
	void shouldReturnInvalidAccountResponseOnNullAccount() {

		WithdrawalRequest request = new WithdrawalRequest();
		WithdrawalResponse response = withdrawalValidationUtil.validate(request);
		assertAccountReponse(response);

		request.setAccountNumber("");
		response = withdrawalValidationUtil.validate(request);
		assertAccountReponse(response);

	}

	private void assertAccountReponse(WithdrawalResponse response) {
		assertThat(response.getResponseCode()).isEqualTo("8");
		assertThat(response.getResponseMessage()).isEqualTo("Account Number is mandatory");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);
	}

	@Test
	@DisplayName("Test return invalid PIN response when PIN is null or empty")
	void shouldReturnInvalidPinResponseOnNullAccount() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("987");
		WithdrawalResponse response = withdrawalValidationUtil.validate(request);
		assertPinResponse(response);

		request.setPin("");
		response = withdrawalValidationUtil.validate(request);
		assertPinResponse(response);

	}

	private void assertPinResponse(WithdrawalResponse response) {
		assertThat(response.getResponseCode()).isEqualTo("9");
		assertThat(response.getResponseMessage()).isEqualTo("PIN is mandatory");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);
	}
	
	@Test
	@DisplayName("Test return invalid amount response when less than zero")
	void shouldReturnInvalidAmountResponseOnLessThanZero() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("987");
		request.setPin("987");
		request.setAmount(-5);
		WithdrawalResponse response = withdrawalValidationUtil.validate(request);
		assertThat(response.getResponseCode()).isEqualTo("11");
		assertThat(response.getAmount()).isEqualTo(request.getAmount());
		assertThat(response.getResponseMessage()).isEqualTo("Invalid withdrawal amount");
		assertThat(response.getResponseStatus()).isEqualTo(FAILED);

	}
	
	@Test
	@DisplayName("Test return successful response")
	void shouldReturnSuccessfulResponse() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("987");
		request.setPin("987");
		request.setAmount(50);
		WithdrawalResponse response = withdrawalValidationUtil.validate(request);
		assertThat(response.getResponseCode()).isEqualTo("0");
		assertThat(response.getAmount()).isEqualTo(request.getAmount());
		assertThat(response.getResponseMessage()).isEqualTo("successful validation");
		assertThat(response.getResponseStatus()).isEqualTo("approved");

	}

}
