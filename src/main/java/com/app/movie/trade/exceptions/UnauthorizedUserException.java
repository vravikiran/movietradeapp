package com.app.movie.trade.exceptions;

public class UnauthorizedUserException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnauthorizedUserException() {
		super();
	}

	public UnauthorizedUserException(String message) {
		super(message);
	}

}
