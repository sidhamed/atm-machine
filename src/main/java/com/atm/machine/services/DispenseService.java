package com.atm.machine.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.machine.exceptions.CommonServiceException;
import com.atm.machine.models.Account;
import com.atm.machine.models.BankNotes;
import com.atm.machine.repositories.BankNotesRepository;

@Service
public class DispenseService {

	@Autowired
	private BankNotesRepository notesRepository;

	public BankNotes edit(BankNotes bankNotes) throws CommonServiceException {

		return notesRepository.saveAndFlush(bankNotes);
	}

	public BankNotes dispense(double amount) throws CommonServiceException {
		List<BankNotes> availableNotes = notesRepository.findAll();
		BankNotes notes;
		BankNotes notesToBeDispensed = new BankNotes();
		double result;
		int remainder;
		if (amount < 5) {
			notesToBeDispensed.setFifty(0);
			notesToBeDispensed.setTwenty(0);
			notesToBeDispensed.setTen(0);
			notesToBeDispensed.setFive(0);
			notesToBeDispensed.setResponseCode("6");
			notesToBeDispensed.setResponseMessage("can't dispense amount less than 5 euros");
			notesToBeDispensed.setResponseStatus("failed");
			return notesToBeDispensed;
		}
		if (amount % 5 != 0) {
			notesToBeDispensed.setFifty(0);
			notesToBeDispensed.setTwenty(0);
			notesToBeDispensed.setTen(0);
			notesToBeDispensed.setFive(0);
			notesToBeDispensed.setResponseCode("7");
			notesToBeDispensed.setResponseMessage("can't dispense amount not divisible by 5");
			notesToBeDispensed.setResponseStatus("failed");
			return notesToBeDispensed;
		}
		if (availableNotes != null) {
			if (!availableNotes.isEmpty()) {
				notes = availableNotes.get(0);

				// select the minimum number of notes for the withdrawal
				int[] dispensibleNotes = { 50, 20, 10, 5 };
				for (int i = 0; i < dispensibleNotes.length; i++) {
					if (amount == 0) {
						break;
					}

					if (amount < dispensibleNotes[i]) {
						continue;
					} else {

						int resdue;
						double fraction;
						switch (i) {
						case 0:
							result = amount / dispensibleNotes[0];
							if (result > notes.getFifty()) {

								amount = Math.abs(result - notes.getFifty()) * dispensibleNotes[0];
								result = notes.getFifty();
							} else {
								amount = 0;
							}
							resdue = (int) result;
							fraction = result - resdue;
							if (fraction != 0) {
								amount = fraction * dispensibleNotes[0];
							}
							notesToBeDispensed.setFifty(resdue);

							break;
						case 1:
							result = amount / dispensibleNotes[1];
							if (result > notes.getTwenty()) {

								amount = Math.abs(result - notes.getTwenty()) * dispensibleNotes[1];
								result = notes.getTwenty();
							} else {
								amount = 0;
							}
							resdue = (int) result;
							fraction = result - resdue;
							if (fraction != 0) {
								amount = fraction * dispensibleNotes[1];
							}
							notesToBeDispensed.setTwenty(resdue);
							break;
						case 2:
							result = amount / dispensibleNotes[2];
							if (result > notes.getTen()) {
								amount = Math.abs(result - notes.getTen()) * dispensibleNotes[2];
								result = notes.getTen();
							} else {
								amount = 0;
							}
							resdue = (int) result;
							fraction = result - resdue;
							if (fraction != 0) {
								amount = fraction * dispensibleNotes[2];
							}
							notesToBeDispensed.setTen(resdue);
							break;
						case 3:
							result = amount / dispensibleNotes[3];
							if (result > notes.getFive()) {
								amount = Math.abs(result - notes.getTen()) * dispensibleNotes[3];
								result = notes.getFive();
							}
							resdue = (int) result;
							fraction = result - resdue;
							if (fraction != 0) {
								amount = fraction * dispensibleNotes[3];
							}
							notesToBeDispensed.setFive(resdue);
							break;

						default:
							break;
						}
					}
				}
				// edit the number of banknotes in the reserve
				notes.setFifty(notes.getFifty() - notesToBeDispensed.getFifty());
				notes.setTwenty(notes.getTwenty() - notesToBeDispensed.getTwenty());
				notes.setTen(notes.getTen() - notesToBeDispensed.getTen());
				notes.setFive(notes.getFive() - notesToBeDispensed.getFive());
				BankNotes editedBankNotes = edit(notes);
				notesToBeDispensed.setResponseCode("0");
				notesToBeDispensed.setResponseMessage("successful dispensing");
				notesToBeDispensed.setResponseStatus("approved");

			}

		}

		return notesToBeDispensed;
	}

}
