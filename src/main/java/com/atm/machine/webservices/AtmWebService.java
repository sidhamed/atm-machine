package com.atm.machine.webservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.machine.exceptions.CommonServiceException;
import com.atm.machine.models.BankNotes;
import com.atm.machine.requests.AccountRequest;
import com.atm.machine.requests.WithdrawalRequest;
import com.atm.machine.responses.AccountResponse;
import com.atm.machine.responses.WithdrawalResponse;
import com.atm.machine.services.AccountService;
import com.atm.machine.services.WithdrawalService;
import com.atm.machine.validation.BalanceValidationUtil;
import com.atm.machine.validation.WithdrawalValidationUtil;

@RestController
public class AtmWebService {
	// this comment is edited from the website of GitHub

	@Autowired
	private AccountService accountService;

	@Autowired
	private WithdrawalService withdrawalService;
	
	@Autowired
	private WithdrawalValidationUtil withdrawalValidationUtil;
	
	@Autowired
	private BalanceValidationUtil balanceValidationUtil;

	@PostMapping("/atm/balance")
	public AccountResponse getBalance(@RequestBody AccountRequest accountRequest) {
		AccountResponse response;
		response = balanceValidationUtil.validate(accountRequest);
		if (!response.getResponseCode().equalsIgnoreCase("0")) {
			response.setCurrency("euro");
			return response;
		}
		try {
			response = accountService.getBalance(accountRequest);
		} catch (CommonServiceException e) {
			response = new AccountResponse();
			response.setResponseCode("7");
			response.setResponseMessage("system failed to retrieve balance");
			response.setResponseStatus("failed");
			return response;
		}
		return response;
	}

	@PostMapping("/atm/withdraw")
	public WithdrawalResponse withdraw(@RequestBody WithdrawalRequest request) {
		WithdrawalResponse response;
		response = withdrawalValidationUtil.validate(request);
		if (!response.getResponseCode().equalsIgnoreCase("0")) {
			response.setCurrency("euro");
			response.setBankNotes(new BankNotes());
			return response;
		}
		try {
			response = withdrawalService.withdraw(request);
		} catch (CommonServiceException e) {
			response = new WithdrawalResponse();
			response.setResponseCode("7");
			response.setResponseMessage("system failed to carry out transaction");
			response.setResponseStatus("failed");
			return response;
		}
		return response;
	}

}
