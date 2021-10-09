package com.atm.machine.webservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
		if (balanceValidationUtil.validationNotSuccessful(response)) {
			response.setCurrency("euro");
			return response;
		}

		response = accountService.getBalance(accountRequest);

		return response;
	}

	@PostMapping("/atm/withdraw")
	public WithdrawalResponse withdraw(@RequestBody WithdrawalRequest request) {
		WithdrawalResponse response;
		response = withdrawalValidationUtil.validate(request);
		if (withdrawalValidationUtil.validationNotSuccessful(response)) {
			response.setCurrency("euro");
			response.setBankNotes(new BankNotes());
			return response;
		}

		response = withdrawalService.withdraw(request);

		return response;
	}

}
