package com.atm.machine.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atm.machine.exceptions.AccountNotFoundException;
import com.atm.machine.exceptions.AmountNotAccessibleException;
import com.atm.machine.exceptions.WrongPINException;
import com.atm.machine.models.Account;
import com.atm.machine.models.BankNotes;
import com.atm.machine.models.DispenseResult;
import com.atm.machine.repositories.AccountRepository;
import com.atm.machine.repositories.BankNotesRepository;
import com.atm.machine.requests.WithdrawalRequest;
import com.atm.machine.responses.WithdrawalResponse;

@Service
public class WithdrawalService {

	private static final String APPROVED = "approved";
	private static final String FAILED = "failed";
	private static final int FIVE_EUROS_BILL = 3;
	private static final int TEN_EUROS_BILL = 2;
	private static final int TWENTY_EUROS_BILL = 1;
	private static final int FIFTY_EUROS_BILL = 0;
	private static final int FIFTY_DOLLARS_BILL = 0;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BankNotesRepository notesRepository;
	private int[] dispensibleNotes = new int[] { 50, 20, 10, 5 };

	public BankNotes edit(BankNotes bankNotes) {

		return notesRepository.saveAndFlush(bankNotes);
	}

	private Account findAccountByAccountNumber(String accountNumber) throws AccountNotFoundException {
		Account retrievedAccount = accountRepository.findByAccountNumber(accountNumber);
		if (retrievedAccount == null) {
			throw new AccountNotFoundException();
		}
		return retrievedAccount;
	}

	private void verifyPIN(WithdrawalRequest request, Account retrievedAccount) throws WrongPINException {
		if (noMatchingPinNumbers(request, retrievedAccount)) {
			throw new WrongPINException();
		}
	}

	private boolean noMatchingPinNumbers(WithdrawalRequest request, Account retrievedAccount) {
		return !request.getPin().equalsIgnoreCase(retrievedAccount.getPin());
	}

	private double checkAmountAccessibility(WithdrawalRequest request, Account retrievedAccount)
			throws AmountNotAccessibleException {
		double accessibleAmount = retrievedAccount.getBalance() + retrievedAccount.getOverdraft();
		if (amountNotAcessible(request, accessibleAmount)) {
			throw new AmountNotAccessibleException(retrievedAccount);
		}
		return accessibleAmount;
	}

	private boolean amountNotAcessible(WithdrawalRequest request, double accessibleAmount) {
		return request.getAmount() > accessibleAmount;
	}

	private WithdrawalResponse updateAccount(WithdrawalRequest request, Account retrievedAccount,
			double accessibleAmount, BankNotes bankNotes) {
		// reflect that on account
		validateWithdrawalRequest(request, retrievedAccount, accessibleAmount);

		WithdrawalResponse response = new WithdrawalResponse();
		if (withdrawalNotSuccessful(bankNotes))
			return generateFailureResponse(request, retrievedAccount, bankNotes);

		enrichResponseWithMessage(response, bankNotes.getResponseCode(), bankNotes.getResponseMessage(),
				bankNotes.getResponseStatus());

		Account edited = accountRepository.saveAndFlush(retrievedAccount);

		generateSuccessResponse(response, edited);

		return response;
	}

	private void generateSuccessResponse(WithdrawalResponse response, Account edited) {
		response.setAccountNumber(edited.getAccountNumber());
		response.setBalance(edited.getBalance());
		response.setOverdraft(edited.getOverdraft());
	}

	private WithdrawalResponse generateFailureResponse(WithdrawalRequest request, Account retrievedAccount,
			BankNotes bankNotes) {
		WithdrawalResponse response = new WithdrawalResponse();
		generateResponse(request, retrievedAccount, bankNotes, response);

		enrichResponseWithMessage(response, bankNotes.getResponseCode(), bankNotes.getResponseMessage(),
				bankNotes.getResponseStatus());
		return response;
	}

	private void generateResponse(WithdrawalRequest request, Account retrievedAccount, BankNotes bankNotes,
			WithdrawalResponse response) {
		response.setAmount(request.getAmount());
		response.setCurrency(retrievedAccount.getCurrency());
		response.setBankNotes(bankNotes);
	}

	private void validateWithdrawalRequest(WithdrawalRequest request, Account retrievedAccount,
			double accessibleAmount) {

		double overdraft = accessibleAmount - request.getAmount();
		double amountDifference = retrievedAccount.getBalance() - request.getAmount();

		if (requestedAmountLessThanBalance(request, retrievedAccount)) {
			retrievedAccount.setBalance(amountDifference);
		}

		if (overdrafting(request, retrievedAccount, accessibleAmount)) {
			retrievedAccount.setBalance(0);
			retrievedAccount.setOverdraft(overdraft);
		}
	}

	private boolean overdrafting(WithdrawalRequest request, Account retrievedAccount, double accessibleAmount) {
		return request.getAmount() > retrievedAccount.getBalance() && request.getAmount() <= accessibleAmount;
	}

	private boolean requestedAmountLessThanBalance(WithdrawalRequest request, Account retrievedAccount) {
		return request.getAmount() < retrievedAccount.getBalance();
	}

	private boolean withdrawalNotSuccessful(BankNotes bankNotes) {
		return !bankNotes.getResponseCode().equalsIgnoreCase("0");
	}

	@Transactional
	public WithdrawalResponse withdraw(WithdrawalRequest request) {
		Account retrievedAccount;
		WithdrawalResponse response = new WithdrawalResponse();
		BankNotes bankNotes;
		try {
			// return
			retrievedAccount = findAccountByAccountNumber(request.getAccountNumber());
			verifyPIN(request, retrievedAccount);
			double accessibleAmount = checkAmountAccessibility(request, retrievedAccount);
			bankNotes = dispense(request.getAmount());
			response = updateAccount(request, retrievedAccount, accessibleAmount, bankNotes);

			generateResponse(request, retrievedAccount, bankNotes, response);

			if (updateNotSuccessful(response))
				return response;

			enrichResponseWithMessage(response, "0", "successful withdrawal and dispense banknotes", APPROVED);

		} catch (AccountNotFoundException e) {
			enrichAccountNotFoundResponse(request, response);
		} catch (WrongPINException e) {
			enrichWrongPINResponse(request, response);
		} catch (AmountNotAccessibleException e) {
			enritchAmountNotAccessibleResponse(request, response, e);
		}
		return response;
	}

	private void enrichAccountNotFoundResponse(WithdrawalRequest request, WithdrawalResponse response) {
		enrichDetailedResponse(request, response);

		response.setCurrency("");

		enrichResponseWithMessage(response, "2", "invalid account number", FAILED);
	}

	private void enrichResponseWithMessage(WithdrawalResponse response, String responseCode, String responseMessage,
			String responseStatus) {
		response.setResponseCode(responseCode);
		response.setResponseMessage(responseMessage);
		response.setResponseStatus(responseStatus);
	}

	private void enrichDetailedResponse(WithdrawalRequest request, WithdrawalResponse response) {
		response.setAccountNumber(request.getAccountNumber());
		response.setAmount(request.getAmount());
		response.setBankNotes(new BankNotes());
	}

	private void enrichWrongPINResponse(WithdrawalRequest request, WithdrawalResponse response) {
		enrichDetailedResponse(request, response);
		enrichResponseWithMessage(response, "1", "invalid pin", FAILED);

	}

	private void enritchAmountNotAccessibleResponse(WithdrawalRequest request, WithdrawalResponse response,
			AmountNotAccessibleException e) {
		enrichDetailedResponse(request, response);

		response.setBalance(e.getAccount().getBalance());
		response.setCurrency(e.getAccount().getCurrency());
		response.setOverdraft(e.getAccount().getOverdraft());

		enrichResponseWithMessage(response, "3", "amount is larger than maximum withdrawal amount", FAILED);

	}

	private boolean updateNotSuccessful(WithdrawalResponse response) {
		return !response.getResponseCode().equalsIgnoreCase("0");
	}

	public BankNotes dispense(double amount) {
		List<BankNotes> availableNotes = notesRepository.findAll(); // get all the available notes from the database
		BankNotes notes = availableNotes.get(0);
		BankNotes notesToBeDispensed = new BankNotes();

		if (amountIsNotValid(amount, notesToBeDispensed)) {
			return notesToBeDispensed;
		}
		if (machineHasBankNotes(availableNotes)) {

			for (int billTypes = 0; billTypes < dispensibleNotes.length; billTypes++) {
				if (amount == 0) {
					break;
				}

				if (amount > dispensibleNotes[billTypes]) {
					DispenseResult dispenseResult = dispenseBills(
							new DispenseOrder(amount, notes, notesToBeDispensed, dispensibleNotes, billTypes));
					amount = dispenseResult.getAmountAfterDispense();
					notesToBeDispensed = dispenseResult.getDispensedNotesSoFar();
				}
			}
			updateMachineNotes(notes, notesToBeDispensed);
			enrichResponseWithMessage(notesToBeDispensed);
		}
		return notesToBeDispensed;
	}

	private void enrichResponseWithMessage(BankNotes notesToBeDispensed) {
		notesToBeDispensed.setResponseCode("0");
		notesToBeDispensed.setResponseMessage("successful dispensing");
		notesToBeDispensed.setResponseStatus(APPROVED);
	}

	private void updateMachineNotes(BankNotes notes, BankNotes notesToBeDispensed) {
		notes.setFifty(getAvailableBills(notes, 0) - getAvailableBills(notesToBeDispensed, 0));
		notes.setTwenty(notes.getTwenty() - notesToBeDispensed.getTwenty());
		notes.setTen(notes.getTen() - notesToBeDispensed.getTen());
		notes.setFive(notes.getFive() - notesToBeDispensed.getFive());
		edit(notes);
	}

	private boolean amountIsNotValid(double amount, BankNotes notesToBeDispensed) {
		validateAmount(amount, notesToBeDispensed);
		return !notesToBeDispensed.getResponseCode().equals("0");
	}

	private void validateAmount(double amount, BankNotes notesToBeDispensed) {
		if (lessThanFiveEuros(amount)) {

			enrichNotesToBeDispensed(notesToBeDispensed, "6", "can't dispense amount less than 5 euros", FAILED);
			return;
		}
		if (notMultipleOfFive(amount)) {

			enrichNotesToBeDispensed(notesToBeDispensed, "7", "can't dispense amount not divisible by 5", FAILED);
			return;
		}

		notesToBeDispensed.setResponseCode("0");

	}

	private boolean machineHasBankNotes(List<BankNotes> availableNotes) {
		return !availableNotes.isEmpty();
	}

	private void enrichNotesToBeDispensed(BankNotes notesToBeDispensed, String responseCode, String responseMessage,
			String responseStatus) {
		notesToBeDispensed.setFifty(0);
		notesToBeDispensed.setTwenty(0);
		notesToBeDispensed.setTen(0);
		notesToBeDispensed.setFive(0);
		notesToBeDispensed.setResponseCode(responseCode);
		notesToBeDispensed.setResponseMessage(responseMessage);
		notesToBeDispensed.setResponseStatus(responseStatus);
	}

	int getAvailableBills(BankNotes notes, int currentBill) {
		if (currentBill == FIFTY_EUROS_BILL) {
			return notes.getFifty();
		}

		if (currentBill == TWENTY_EUROS_BILL) {
			return notes.getTwenty();
		}

		if (currentBill == TEN_EUROS_BILL) {
			return notes.getTen();
		}

		if (currentBill == FIVE_EUROS_BILL) {
			return notes.getFive();
		}

		return -1;

	}

	private boolean lessThanFiveEuros(double amount) {
		return amount < 5;
	}

	private boolean notMultipleOfFive(double amount) {
		return amount % 5 != 0;
	}

	protected DispenseResult dispenseBills(DispenseOrder dispenseOrder) {
		double amount = 0;
		Double numberOfBillsToBeDispensed = determineNumberOfBilles(dispenseOrder, dispenseOrder.getAmount());
		int availableBills = getAvailableBills(dispenseOrder.getNotes(), dispenseOrder.getCurrentBill());

		if (numberOfBillsToBeDispensed > availableBills) {
			amount = calculateAmountDispensed(dispenseOrder, numberOfBillsToBeDispensed, availableBills);
			numberOfBillsToBeDispensed = Double.valueOf(availableBills);
		}

		dispenseOrder.getNotesToBeDispensed().setFifty(numberOfBillsToBeDispensed.intValue());
		return new DispenseResult(amount, dispenseOrder.getNotesToBeDispensed());

	}

	private double calculateAmountDispensed(DispenseOrder dispenseOrder, double numberOfBillsToBeDispensed,
			int availableBills) {
		return Math.abs(numberOfBillsToBeDispensed - availableBills)
				* dispenseOrder.getDispensibleNotes()[dispenseOrder.getCurrentBill()];
	}

	private double determineNumberOfBilles(DispenseOrder dispenseOrder, double amount) {
		return amount / dispenseOrder.getDispensibleNotes()[dispenseOrder.getCurrentBill()];
	}

}
