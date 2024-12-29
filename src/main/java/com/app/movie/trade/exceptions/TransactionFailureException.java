package com.app.movie.trade.exceptions;

public class TransactionFailureException extends Exception {

	private static final long serialVersionUID = 1L;

	public TransactionFailureException() {
		super();
	}

	public TransactionFailureException(String message) {
		super(message);
	}

}
