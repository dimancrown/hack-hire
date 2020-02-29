package com.scb.cos.hk.ms.commons.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{
    private HttpStatus status;
    private List<String> errors;
    
	public CustomException(HttpStatus status, String message, String error, Throwable ex) {
		super(message, ex);
		this.status = status;
		this.errors = Arrays.asList(error);
	}
	
	public CustomException(HttpStatus status, String message, List<String> errors, Throwable ex) {
		super(message, ex);
		this.status = status;
		this.errors = errors;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public List<String> getErrors() {
		return errors;
	}
	
}
