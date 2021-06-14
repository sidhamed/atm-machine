package com.atm.machine.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
		
		List<Account> list = new ArrayList<Account>();
		list.add(one);
		list.add(two);

		
		
		
		
		Account deleted = null;

		lenient().when(accountRepository.findByAccountNumber("123456789")).thenReturn(one);
		lenient().when(accountRepository.findByAccountNumber("987654321")).thenReturn(two);
		lenient().when(accountRepository.findByAccountNumber("9876543210")).thenReturn(deleted);
		
		try {
			
			lenient().when(accountService.findAll()).thenReturn(list);
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Test successful findByAccountNumber")
	void shouldGetAccountByPhoneNumber() {

		Account account = new Account();
		account.setAccountNumber("123456789");

		try {
			Account recieved = accountService.findByAccountNumber(account);
			assertThat(recieved.getAccountNumber()).isEqualTo("123456789");

		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	@DisplayName("Test successful findByAccountNumber Account not found")
	void shouldNotGetAccountByPhoneNumber() {

		Account account = new Account();
		account.setAccountNumber("1234567");

		try {
			Account recieved = accountService.findByAccountNumber(account);
			assertThat(recieved).isNull();

		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	@DisplayName("Test successful save a new Account ")
	void shouldSaveAccountAndReturnIt() {

		Account account = new Account();
		account.setAccountNumber("987654321");
		account.setPin("4321");
		account.setBalance(1230);
		account.setOverdraft(150);
		account.setStatus("Active");
		account.setDateCreated(new Date());
		account.setCurrency("euro");
		
		

		try {
			lenient().when(accountService.save(account)).thenReturn(account);
			Account recieved = accountService.save(account);
			assertThat(recieved).isNotNull();
			assertThat(recieved.getAccountNumber()).isEqualTo("987654321");

		} catch (CommonServiceException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful deletion by ID")
	void shouldDeleteAccount() {

		Account account = new Account();
		account.setAccountNumber("9876543210");
		account.setPin("4321");
		account.setBalance(1230);
		account.setOverdraft(150);
		account.setStatus("Active");
		account.setDateCreated(new Date());
		account.setCurrency("euro");
		account.setSystemId((long) 25);

		try {
			accountService.deleteById(account.getSystemId());
			Account deleted = accountService.findByAccountNumber(account);
			assertThat(deleted).isNull();

		} catch (CommonServiceException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful find all accounts")
	void shouldReturnAllAccounts() {

		try {
			List<Account> returnedList = accountService.findAll();
			assertThat(returnedList).isNotNull();
			assertThat(returnedList).isNotEmpty();

		} catch (CommonServiceException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful edit account")
	void shouldEditAndReturnEditAccount() {
		
		Account account = new Account();
		account.setAccountNumber("987654321");
		account.setPin("4321");
		account.setBalance(12300);
		account.setOverdraft(150);
		account.setStatus("Active");
		account.setDateCreated(new Date());
		account.setCurrency("euro");
		
		


		try {
			lenient().when(accountService.edit(account)).thenReturn(account);
			Account editedAccount = accountService.edit(account);
			assertThat(editedAccount).isNotNull();
			assertThat(editedAccount).isEqualTo(account);

		} catch (CommonServiceException e) {
			e.printStackTrace();
		}

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
