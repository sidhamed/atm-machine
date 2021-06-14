package com.atm.machine.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atm.machine.exceptions.CommonServiceException;
import com.atm.machine.models.Account;
import com.atm.machine.models.BankNotes;
import com.atm.machine.repositories.AccountRepository;
import com.atm.machine.repositories.BankNotesRepository;
import com.atm.machine.requests.WithdrawalRequest;
import com.atm.machine.responses.WithdrawalResponse;

@Service
public class WithdrawalService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BankNotesRepository notesRepository;

	public BankNotes edit(BankNotes bankNotes) throws CommonServiceException {

		return notesRepository.saveAndFlush(bankNotes);
	}

	@Transactional
	public WithdrawalResponse withdraw(WithdrawalRequest request) throws CommonServiceException {
		Account retrievedAccount = accountRepository.findByAccountNumber(request.getAccountNumber());
		WithdrawalResponse response = new WithdrawalResponse();
		// make sure there's an account
		if (retrievedAccount == null) {
			response.setAccountNumber(request.getAccountNumber());
			response.setAmount(request.getAmount());
			response.setCurrency("");
			response.setResponseCode("2");
			response.setBankNotes(new BankNotes());
			response.setResponseMessage("invalid account number");
			response.setResponseStatus("failed");
		} else {
			// check if pins match
			if (request.getPin().equalsIgnoreCase(retrievedAccount.getPin())) {
				double accessibleAmount = retrievedAccount.getBalance() + retrievedAccount.getOverdraft();
				// if user has access to enough money to cover the withdrawal transaction
				if (request.getAmount() <= accessibleAmount) {
					// dispense money
					BankNotes bankNotes = dispense(request.getAmount());
					// successfully dispensed
					if (bankNotes.getResponseCode().equalsIgnoreCase("0")) {
						// reflect that on account
						if (request.getAmount() < retrievedAccount.getBalance()) {
							retrievedAccount.setBalance(retrievedAccount.getBalance() - request.getAmount());
						} else if (request.getAmount() > retrievedAccount.getBalance()
								&& request.getAmount() <= accessibleAmount) {
							retrievedAccount.setBalance(0);
							retrievedAccount.setOverdraft(accessibleAmount - request.getAmount());
						}

						response.setAmount(request.getAmount());
						response.setCurrency(retrievedAccount.getCurrency());
						response.setBankNotes(bankNotes);
						response.setResponseCode("0");
						response.setResponseMessage("successful withdrawal and dispense banknotes");
						response.setResponseStatus("approved");

						Account edited = accountRepository.saveAndFlush(retrievedAccount);
						response.setAccountNumber(edited.getAccountNumber());
						response.setBalance(edited.getBalance());
						response.setOverdraft(edited.getOverdraft());

					} else {
						// problem while dispensing notes
						response.setAccountNumber(request.getAccountNumber());
						response.setAmount(request.getAmount());
						response.setBankNotes(new BankNotes());
						response.setBalance(retrievedAccount.getBalance());
						response.setCurrency(retrievedAccount.getCurrency());
						response.setOverdraft(retrievedAccount.getOverdraft());
						response.setResponseCode(bankNotes.getResponseCode());
						response.setResponseMessage(bankNotes.getResponseMessage());
						response.setResponseStatus(bankNotes.getResponseStatus());
					}

				} else {
					// withdrawal amount not valid
					response.setAccountNumber(request.getAccountNumber());
					response.setAmount(request.getAmount());
					response.setBalance(retrievedAccount.getBalance());
					response.setCurrency(retrievedAccount.getCurrency());
					response.setOverdraft(retrievedAccount.getOverdraft());
					response.setBankNotes(new BankNotes());
					response.setResponseCode("3");
					response.setResponseMessage("amount is larger than maximum withdrawal amount");
					response.setResponseStatus("failed");
				}

			} else {

				response.setAccountNumber(request.getAccountNumber());
				response.setAmount(request.getAmount());
				response.setBankNotes(new BankNotes());
				response.setResponseCode("1");
				response.setResponseMessage("invalid pin");
				response.setResponseStatus("failed");

			}

		}
		return response;
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
