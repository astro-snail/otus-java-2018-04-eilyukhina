package ru.otus.l051.tester;

public enum TestResult {

	NOT_RUN("Test not run yet"), SUCCESS("Test was successful"), FAILURE("Test failed");

	private String message;

	private TestResult(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
