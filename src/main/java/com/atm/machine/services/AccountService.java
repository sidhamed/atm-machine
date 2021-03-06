package com.atm.machine.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.machine.models.Account;
import com.atm.machine.repositories.AccountRepository;
import com.atm.machine.responses.AccountResponse;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public Account save(Account account) {
		account.setStatus("Active");
		account.setDateCreated(new Date());
		account.setCurrency("euro");
		return accountRepository.save(account);
	}

	public Account edit(Account account) {

		return accountRepository.saveAndFlush(account);
	}

	public Account findByAccountNumber(Account account) {
		return accountRepository.findByAccountNumber(account.getAccountNumber());
	}

	public void deleteById(Long id) {
		accountRepository.deleteById(id);

	}

	public List<Account> findAll() {

		return accountRepository.findAll();
	}

	public AccountResponse getBalance(Account account) {
		Account retrievedAccount = accountRepository.findByAccountNumber(account.getAccountNumber());
		AccountResponse response = new AccountResponse();

		if (retrievedAccount == null) {
			response.setAccountNumber(account.getAccountNumber());
			response.setResponseCode("2");
			response.setResponseMessage("invalid account number");
			response.setResponseStatus("failed");
		} else {
			// check if pins match
			if (account.getPin().equalsIgnoreCase(retrievedAccount.getPin())) {
				response.setAccountNumber(account.getAccountNumber());
				response.setOverdraft(retrievedAccount.getOverdraft());
				response.setBalance(retrievedAccount.getBalance());
				response.setMaximumWithdrawalAmount(response.getBalance() + response.getOverdraft());
				response.setResponseCode("0");
				response.setCurrency("euro");
				response.setResponseMessage("successful balance inquiry");
				response.setResponseStatus("approved");
			} else {

				response.setAccountNumber(account.getAccountNumber());
				response.setResponseCode("1");
				response.setResponseMessage("invalid pin");
				response.setResponseStatus("failed");

			}
		}

		return response;
	}
}
