package ru.otus.l051.validations;

public class AssertionFailedError extends AssertionError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AssertionFailedError() {
		super();
	}

	public AssertionFailedError(String message) {
		super(message);
	}

}
