package com.atm.machine.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.atm.machine.exceptions.CommonServiceException;
import com.atm.machine.models.Account;
import com.atm.machine.repositories.AccountRepository;
import com.atm.machine.responses.AccountResponse;
import com.atm.machine.services.AccountService;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountService accountService;

	@BeforeEach
	public void init() {

		Account one = new Account();
		one.setAccountNumber("123456789");
		one.setPin("1234");
		one.setBalance(800);
		one.setOverdraft(200);

		Account two = new Account();
		two.setAccountNumber("987654321");
		two.setPin("4321");
		two.setBalance(1230);
		two.setOverdraft(150);

		
		lenient().when(accountRepository.findByAccountNumber("123456789")).thenReturn(one);
		lenient().when(accountRepository.findByAccountNumber("987654321")).thenReturn(one);

	}

	@Test
	@DisplayName("Test failed Balance Inquiry for Wrong Accont Number")
	void shouldNotFindAccountBecauseAccountIsWrongTest() {

		Account account = new Account();
		account.setAccountNumber("8998");
		account.setPin("98");

		try {
			AccountResponse recieved = accountService.getBalance(account);
			assertThat(recieved.getResponseCode()).isEqualTo("2");
			assertThat(recieved.getResponseMessage()).isEqualTo("invalid account number");
			assertThat(recieved.getResponseStatus()).isEqualTo("failed");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test Successful Balance Inquiry")
	void shouldReturnBalanceSuccessfullyTest() {

		Account account = new Account();
		account.setAccountNumber("123456789");
		account.setPin("1234");

		try {
			AccountResponse recieved = accountService.getBalance(account);
			assertThat(recieved.getResponseCode()).isEqualTo("0");
			assertThat(recieved.getResponseMessage()).isEqualTo("successful balance inquiry");
			assertThat(recieved.getResponseStatus()).isEqualTo("approved");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	@DisplayName("Test failed Balance Inquiry for wrong PIN")
	void wrongPinTest() {

		Account account = new Account();
		account.setAccountNumber("987654321");
		account.setPin("98");

		try {
			AccountResponse recieved = accountService.getBalance(account);
			assertThat(recieved.getResponseCode()).isEqualTo("1");
			assertThat(recieved.getResponseMessage()).isEqualTo("invalid pin");
			assertThat(recieved.getResponseStatus()).isEqualTo("failed");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
