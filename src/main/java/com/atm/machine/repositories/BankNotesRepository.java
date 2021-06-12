package com.atm.machine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.machine.models.BankNotes;


@Repository
public interface BankNotesRepository extends JpaRepository<BankNotes, Long>{
	

	
}
