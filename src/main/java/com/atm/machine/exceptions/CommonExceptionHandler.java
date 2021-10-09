package com.atm.machine.exceptions;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javassist.NotFoundException;

@ControllerAdvice
public class CommonExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

	@ExceptionHandler(NotFoundException.class)
	public String handleNotFoundException() {
		logger.debug("Not found Exception happen");
		return "error/error";
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public String handleSQLException(HttpServletRequest request, Exception ex) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("SQLException Occured:: URL=%s", request.getRequestURL()) );

		}
		return "error/error";
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "IOException occured")
	@ExceptionHandler(IOException.class)
	public void handleIOException() {
		logger.debug("IOException handler executed");
	}

	
}
