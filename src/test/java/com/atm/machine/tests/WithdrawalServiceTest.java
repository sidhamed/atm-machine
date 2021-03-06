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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.atm.machine.models.Account;
import com.atm.machine.models.BankNotes;
import com.atm.machine.repositories.AccountRepository;
import com.atm.machine.repositories.BankNotesRepository;
import com.atm.machine.requests.WithdrawalRequest;
import com.atm.machine.responses.WithdrawalResponse;
import com.atm.machine.services.WithdrawalService;

@ExtendWith(MockitoExtension.class)
class WithdrawalServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private WithdrawalService withdrawalService;

	@Mock
	private BankNotesRepository bankNotesRepository;

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

		BankNotes initNotes = new BankNotes();
		initNotes.setFifty(10);
		initNotes.setTwenty(30);
		initNotes.setTen(30);
		initNotes.setFive(20);

		List<BankNotes> list = new ArrayList<BankNotes>();
		list.add(initNotes);

		BankNotes notes200 = new BankNotes();
		notes200.setFifty(4);
		notes200.setResponseCode("0");

		BankNotes notes900 = new BankNotes();
		notes900.setFifty(8);
		notes900.setResponseCode("0");

		Account account = new Account();
		account.setAccountNumber("123456789");
		account.setPin("1234");
		account.setBalance(600);
		account.setOverdraft(200);
		account.setStatus("Active");
		account.setDateCreated(new Date());
		account.setCurrency("euro");

		lenient().when(accountRepository.findByAccountNumber("123456789")).thenReturn(one);
		lenient().when(accountRepository.findByAccountNumber("987654321")).thenReturn(two);
		lenient().when(bankNotesRepository.findAll()).thenReturn(list);

		lenient().when(withdrawalService.dispense(200)).thenReturn(notes200);
		lenient().when(withdrawalService.dispense(900)).thenReturn(notes900);
		lenient().when(accountRepository.saveAndFlush(one)).thenReturn(account);

	}

	@Test
	@DisplayName("Test failed withdrawal for Wrong Accont Number")
	void shouldNotFindAccountBecauseAccountIsWrongTest() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("123456");
		request.setPin("1234");

		WithdrawalResponse recieved = withdrawalService.withdraw(request);
		assertThat(recieved.getResponseCode()).isEqualTo("2");
		assertThat(recieved.getResponseMessage()).isEqualTo("invalid account number");
		assertThat(recieved.getResponseStatus()).isEqualTo("failed");

	}

	@Test
	@DisplayName("Test failed withdrawal for wrong PIN")
	void wrongPinTest() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("123456789");
		request.setPin("12");

		WithdrawalResponse recieved = withdrawalService.withdraw(request);
		assertThat(recieved.getResponseCode()).isEqualTo("1");
		assertThat(recieved.getResponseMessage()).isEqualTo("invalid pin");
		assertThat(recieved.getResponseStatus()).isEqualTo("failed");

	}

	@Test
	@DisplayName("Test failed withdrawal for Amount larger than maximum allowed amount")
	void largerThanAccessibleAmount() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("123456789");
		request.setPin("1234");
		request.setAmount(5000);

		WithdrawalResponse recieved = withdrawalService.withdraw(request);
		assertThat(recieved.getResponseCode()).isEqualTo("3");
		assertThat(recieved.getResponseMessage()).isEqualTo("amount is larger than maximum withdrawal amount");
		assertThat(recieved.getResponseStatus()).isEqualTo("failed");

	}

	@Test
	@DisplayName("Test failed withdrawal for amount not dispensible")
	void failedWithdrawalForBadAmount() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("123456789");
		request.setPin("1234");
		request.setAmount(3);

		WithdrawalResponse recieved = withdrawalService.withdraw(request);
		assertThat(recieved.getResponseCode()).isNotEqualTo("0");
		assertThat(recieved.getResponseStatus()).isEqualTo("failed");

	}

	@Test
	@DisplayName("Test successful withdrawal for amount less than balance")
	void sucessfulWithdawalLessThanBalance() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("123456789");
		request.setPin("1234");
		request.setAmount(200);

		WithdrawalResponse recieved = withdrawalService.withdraw(request);
		assertThat(recieved.getResponseCode()).isEqualTo("0");
		assertThat(recieved.getResponseStatus()).isEqualTo("approved");

	}

	@Test
	@DisplayName("Test successful withdrawal for amount more than balance")
	void sucessfulWithdawalMoreThanBalance() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("123456789");
		request.setPin("1234");
		request.setAmount(900);

		WithdrawalResponse recieved = withdrawalService.withdraw(request);
		assertThat(recieved.getResponseCode()).isEqualTo("0");
		assertThat(recieved.getResponseStatus()).isEqualTo("approved");

	}

	@Test
	@DisplayName("Test failed dispense amount less than five")
	void ShouldNotDispenseWhenAmountLessThanFive() {

		double amount = 3.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("6");
		assertThat(notes.getResponseMessage()).isEqualTo("can't dispense amount less than 5 euros");
		assertThat(notes.getResponseStatus()).isEqualTo("failed");

	}

	@Test
	@DisplayName("Test failed dispense amount not divisible by five")
	void ShouldNotDispenseWhenAmoutNotDivisibleByFive() {

		double amount = 7;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("7");
		assertThat(notes.getResponseMessage()).isEqualTo("can't dispense amount not divisible by 5");
		assertThat(notes.getResponseStatus()).isEqualTo("failed");

	}

	@Test
	@DisplayName("Test successful dispensing amount with all notes")
	void successfulDispensingAllNotes() {

		double amount = 1305.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("0");
		assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
		assertThat(notes.getResponseStatus()).isEqualTo("approved");

	}

	@Test
	@DisplayName("Test failed when amount equals zero")
	void shouldNotDispenseWhenAmountEqualsZero() {

		double amount = 0.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("6");
		assertThat(notes.getResponseMessage()).isEqualTo("can't dispense amount less than 5 euros");
		assertThat(notes.getResponseStatus()).isEqualTo("failed");

	}

	@Test
	@DisplayName("Test successful dispensing less than number of 50 euros notes in the machine")
	void shouldDispenseSuccessfulLessThan50() {

		double amount = 200.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("0");
		assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
		assertThat(notes.getResponseStatus()).isEqualTo("approved");

	}

	@Test
	@DisplayName("Test successful dispensing less than number of 20 euros notes in the machine")
	void shouldDispenseSuccessfulLessThan20() {

		double amount = 40.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("0");
		assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
		assertThat(notes.getResponseStatus()).isEqualTo("approved");

	}

	@Test
	@DisplayName("Test successful dispensing less than number of 10 euros notes in the machine")
	void shouldDispenseSuccessfulLessThan10() {

		double amount = 10.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("0");
		assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
		assertThat(notes.getResponseStatus()).isEqualTo("approved");

	}

	@Test
	@DisplayName("Test successful dispensing less than number of 5 euros notes in the machine")
	void shouldDispenseSuccessfulLessThan5() {

		double amount = 5.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("0");
		assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
		assertThat(notes.getResponseStatus()).isEqualTo("approved");

	}

	@Test
	@DisplayName("Test successful dispensing between 50 and 20")
	void shouldDispenseSuccessfulBetween50and20() {

		double amount = 1500.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("0");
		assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
		assertThat(notes.getResponseStatus()).isEqualTo("approved");

	}

	@Test
	@DisplayName("Test successful dispensing between 10 and 5")
	void shouldDispenseSuccessfulBetween10and5() {

		double amount = 1700.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		assertThat(notes.getResponseCode()).isEqualTo("0");
		assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
		assertThat(notes.getResponseStatus()).isEqualTo("approved");

	}

	@Test
	@DisplayName("Test failed no banknotes")
	void shouldNotDispenseMachineIsEmpty() {

		double amount = 1500.0;
		BankNotes notes;

		notes = withdrawalService.dispense(amount);
		notes = withdrawalService.dispense(amount); // repeat
		assertThat(notes.getResponseCode()).isEqualTo("0");
		assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
		assertThat(notes.getResponseStatus()).isEqualTo("approved");

	}

}
