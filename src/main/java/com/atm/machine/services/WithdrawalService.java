package com.atm.machine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atm.machine.exceptions.CommonServiceException;
import com.atm.machine.models.Account;
import com.atm.machine.models.BankNotes;
import com.atm.machine.repositories.AccountRepository;
import com.atm.machine.requests.WithdrawalRequest;
import com.atm.machine.responses.WithdrawalResponse;

@Service
public class WithdrawalService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private DispenseService dispenseService;

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
					BankNotes bankNotes = dispenseService.dispense(request.getAmount());
					// successfully dispensed
					if (bankNotes.getResponseCode().equalsIgnoreCase("0")) {
						// reflect that on account
						if (request.getAmount() < retrievedAccount.getBalance()) {
							retrievedAccount.setBalance(retrievedAccount.getBalance() - request.getAmount());
						}
						else if (request.getAmount() > retrievedAccount.getBalance() && request.getAmount() <= accessibleAmount) {
							retrievedAccount.setBalance(0);
							retrievedAccount.setOverdraft(accessibleAmount - request.getAmount());
						}
						
						Account edited = accountService.edit(retrievedAccount);
						response.setBankNotes(bankNotes);
						response.setAccountNumber(edited.getAccountNumber());
						response.setAmount(request.getAmount());
						response.setCurrency(retrievedAccount.getCurrency());
						response.setBalance(edited.getBalance());
						response.setOverdraft(edited.getOverdraft());
						response.setResponseCode("0");
						response.setResponseMessage("successful withdrawal and dispense banknotes");
						response.setResponseStatus("approved");
					}
					else
					{
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
					
				}
				else
				{
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

}
