package com.atm.machine.init;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.atm.machine.models.Account;
import com.atm.machine.models.BankNotes;
import com.atm.machine.repositories.BankNotesRepository;
import com.atm.machine.services.AccountService;

@Component
public class AtmMachineInit {

	@Autowired
	private AccountService accountService;

	@Autowired
	private BankNotesRepository bankNotesRepositotry;

	@PostConstruct
	@Transactional
	public void initializeAtmMachine() {
		
		// delete if already found
		List<Account> deletedAccounts = accountService.findAll();
		for (Account account : deletedAccounts) {
			accountService.deleteById(account.getSystemId());
		}
		
		// delete notes record
		bankNotesRepositotry.deleteAll();
		
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
		
		accountService.save(one);
		accountService.save(two);
		
		BankNotes initNotes = new BankNotes();
		initNotes.setFifty(10);
		initNotes.setTwenty(30);
		initNotes.setTen(30);
		initNotes.setFive(20);
		
		bankNotesRepositotry.save(initNotes);

	}

}
