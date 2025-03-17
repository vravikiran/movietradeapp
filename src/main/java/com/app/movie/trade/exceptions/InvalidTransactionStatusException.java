package com.app.movie.trade.exceptions;

public class InvalidTransactionStatusException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidTransactionStatusException() {
		super();
	}

	public InvalidTransactionStatusException(String message) {
		super(message);
	}

}
