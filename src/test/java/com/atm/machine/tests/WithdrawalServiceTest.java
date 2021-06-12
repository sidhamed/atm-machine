package com.atm.machine.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.atm.machine.exceptions.CommonServiceException;
import com.atm.machine.models.Account;
import com.atm.machine.models.BankNotes;
import com.atm.machine.repositories.AccountRepository;
import com.atm.machine.repositories.BankNotesRepository;
import com.atm.machine.requests.WithdrawalRequest;
import com.atm.machine.responses.AccountResponse;
import com.atm.machine.responses.WithdrawalResponse;
import com.atm.machine.services.AccountService;
import com.atm.machine.services.DispenseService;
import com.atm.machine.services.WithdrawalService;

@ExtendWith(MockitoExtension.class)
public class WithdrawalServiceTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private AccountService accountService;

	@Mock
	private DispenseService dispenseService;

	@InjectMocks
	private WithdrawalService withdrawalService;

	@Mock
	private BankNotesRepository bankNotesRepositotry;

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

		lenient().when(accountRepository.findByAccountNumber("123456789")).thenReturn(one);
		lenient().when(accountRepository.findByAccountNumber("987654321")).thenReturn(two);
		lenient().when(bankNotesRepositotry.findAll()).thenReturn(list);

	}

	@Test
	@DisplayName("Test failed withdrawal for Wrong Accont Number")
	void shouldNotFindAccountBecauseAccountIsWrongTest() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("123456");
		request.setPin("1234");

		try {
			WithdrawalResponse recieved = withdrawalService.withdraw(request);
			assertThat(recieved.getResponseCode()).isEqualTo("2");
			assertThat(recieved.getResponseMessage()).isEqualTo("invalid account number");
			assertThat(recieved.getResponseStatus()).isEqualTo("failed");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	@DisplayName("Test failed withdrawal for wrong PIN")
	void wrongPinTest() {

		WithdrawalRequest request = new WithdrawalRequest();
		request.setAccountNumber("123456789");
		request.setPin("1234");

		try {
			WithdrawalResponse recieved = withdrawalService.withdraw(request);
			assertThat(recieved.getResponseCode()).isEqualTo("1");
			assertThat(recieved.getResponseMessage()).isEqualTo("invalid pin");
			assertThat(recieved.getResponseStatus()).isEqualTo("failed");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
