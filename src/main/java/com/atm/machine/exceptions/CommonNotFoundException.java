package com.atm.machine.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource Not Found !")
public class CommonNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4249915497259658443L;

}
