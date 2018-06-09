package ru.otus.l051.validations;

public class ExpectedExceptionError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ExpectedExceptionError() {
		super();
	}
	
	public ExpectedExceptionError(String message) {
		super(message);
	}
}
