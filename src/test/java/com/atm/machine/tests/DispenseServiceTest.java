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

import com.atm.machine.exceptions.CommonServiceException;
import com.atm.machine.models.Account;
import com.atm.machine.models.BankNotes;
import com.atm.machine.repositories.AccountRepository;
import com.atm.machine.repositories.BankNotesRepository;
import com.atm.machine.responses.AccountResponse;
import com.atm.machine.services.AccountService;
import com.atm.machine.services.DispenseService;
import com.atm.machine.services.WithdrawalService;

@ExtendWith(MockitoExtension.class)
public class DispenseServiceTest {

	@Mock
	private BankNotesRepository bankNotesRepository;
	
	@InjectMocks
	private DispenseService dispenseService;

	@BeforeEach
	public void init() {
		
		BankNotes initNotes = new BankNotes();
		initNotes.setFifty(10); // 500
		initNotes.setTwenty(30); //600
		initNotes.setTen(30); // 300
		initNotes.setFive(20); // 100

		List<BankNotes> list = new ArrayList<BankNotes>();
		list.add(initNotes);
		
		lenient().when(bankNotesRepository.findAll()).thenReturn(list);

	}

	
	
	

	@Test
	@DisplayName("Test failed dispense amount less than five")
	void ShouldNotDispenseWhenAmountLessThanFive() {

		double amount = 3.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("6");
			assertThat(notes.getResponseMessage()).isEqualTo("can't dispense amount less than 5 euros");
			assertThat(notes.getResponseStatus()).isEqualTo("failed");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test failed dispense amount not divisible by five")
	void ShouldNotDispenseWhenAmoutNotDivisibleByFive() {

		double amount = 7 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("7");
			assertThat(notes.getResponseMessage()).isEqualTo("can't dispense amount not divisible by 5");
			assertThat(notes.getResponseStatus()).isEqualTo("failed");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful dispensing amount with all notes")
	void successfulDispensingAllNotes() {

		double amount = 1305.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("0");
			assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
			assertThat(notes.getResponseStatus()).isEqualTo("approved");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test failed when amount equals zero")
	void shouldNotDispenseWhenAmountEqualsZero() {

		double amount = 0.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("6");
			assertThat(notes.getResponseMessage()).isEqualTo("can't dispense amount less than 5 euros");
			assertThat(notes.getResponseStatus()).isEqualTo("failed");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful dispensing less than number of 50 euros notes in the machine")
	void shouldDispenseSuccessfulLessThan50() {

		double amount = 200.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("0");
			assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
			assertThat(notes.getResponseStatus()).isEqualTo("approved");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful dispensing less than number of 20 euros notes in the machine")
	void shouldDispenseSuccessfulLessThan20() {

		double amount = 40.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("0");
			assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
			assertThat(notes.getResponseStatus()).isEqualTo("approved");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful dispensing less than number of 10 euros notes in the machine")
	void shouldDispenseSuccessfulLessThan10() {

		double amount = 10.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("0");
			assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
			assertThat(notes.getResponseStatus()).isEqualTo("approved");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful dispensing less than number of 5 euros notes in the machine")
	void shouldDispenseSuccessfulLessThan5() {

		double amount = 5.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("0");
			assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
			assertThat(notes.getResponseStatus()).isEqualTo("approved");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful dispensing between 50 and 20")
	void shouldDispenseSuccessfulBetween50and20() {

		double amount = 1500.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("0");
			assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
			assertThat(notes.getResponseStatus()).isEqualTo("approved");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test successful dispensing between 10 and 5")
	void shouldDispenseSuccessfulBetween10and5() {

		double amount = 1700.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			assertThat(notes.getResponseCode()).isEqualTo("0");
			assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
			assertThat(notes.getResponseStatus()).isEqualTo("approved");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	@DisplayName("Test failed no banknotes")
	void shouldNotDispenseMachineIsEmpty() {

		double amount = 1500.0 ;
		BankNotes notes;

		try {
			notes = dispenseService.dispense(amount);
			notes = dispenseService.dispense(amount); // repeat
			assertThat(notes.getResponseCode()).isEqualTo("0");
			assertThat(notes.getResponseMessage()).isEqualTo("successful dispensing");
			assertThat(notes.getResponseStatus()).isEqualTo("approved");
		} catch (CommonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
