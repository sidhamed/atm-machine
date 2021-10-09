package com.atm.machine.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.atm.machine.models.BankNotes;
import com.atm.machine.services.DispenseOrder;

@ExtendWith(MockitoExtension.class)
class DispenseBillsDetailsTest {

	@InjectMocks
	private DispenseOrder dispenseBillsDetails;

	@Test
	@DisplayName("Test amount setter/getter lombok annotation")
	void shouldReturnAmount() {

		dispenseBillsDetails.setAmount(50);
		assertThat(dispenseBillsDetails.getAmount()).isEqualTo(50.0);

	}
	
	@Test
	@DisplayName("Test current bill setter/getter lombok annotation")
	void shouldReturnCurrentBill() {

		dispenseBillsDetails.setCurrentBill(50);
		assertThat(dispenseBillsDetails.getCurrentBill()).isEqualTo(50);

	}
	
	@Test
	@DisplayName("Test notes setter/getter lombok annotation")
	void shouldReturnNotes() {

		BankNotes notes = new BankNotes();
		dispenseBillsDetails.setNotes(notes);
		assertThat(dispenseBillsDetails.getNotes()).isEqualTo(notes);

	}
	
	@Test
	@DisplayName("Test notes to be dispensed setter/getter lombok annotation")
	void shouldReturnNotesToBeDispensed() {

		BankNotes notes = new BankNotes();
		dispenseBillsDetails.setNotes(notes);
		assertThat(dispenseBillsDetails.getNotes()).isEqualTo(notes);

	}
	
	@Test
	@DisplayName("Test dispensible notes setter/getter lombok annotation")
	void shouldReturnDispensibleNotes() {

		int[] notes = new int[3];
		dispenseBillsDetails.setDispensibleNotes(notes);
		assertThat(dispenseBillsDetails.getDispensibleNotes()).isEqualTo(notes);

	}

	
}
