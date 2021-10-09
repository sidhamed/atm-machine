package com.atm.machine.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.atm.machine.requests.WithdrawalRequest;

@ExtendWith(MockitoExtension.class)
class WithdrawalRequestTest {

	@InjectMocks
	private WithdrawalRequest withdrawalRequest;

	@Test
	@DisplayName("Test account number setter/getter lombok annotation")
	void shouldReturnAccountNumber() {

		withdrawalRequest.setAccountNumber("987");
		assertThat(withdrawalRequest.getAccountNumber()).isEqualTo("987");

	}
	
	@Test
	@DisplayName("Test PIN setter/getter lombok annotation")
	void shouldReturnPIN() {

		withdrawalRequest.setPin("987");
		assertThat(withdrawalRequest.getPin()).isEqualTo("987");

	}
	
	@Test
	@DisplayName("Test amount setter/getter lombok annotation")
	void shouldReturnAmount() {

		withdrawalRequest.setAmount(5.0);
		assertThat(withdrawalRequest.getAmount()).isEqualTo(5.0);

	}

	
}
