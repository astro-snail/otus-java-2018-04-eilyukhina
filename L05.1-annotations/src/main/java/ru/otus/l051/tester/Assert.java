package ru.otus.l051.tester;

import ru.otus.l051.validations.AssertionFailedError;

public class Assert {

	private Assert() {

	}

	public static void assertTrue(boolean condition) {
		if (!condition) {
			fail("Condition is not true");
		}

	}

	public static void assertFalse(boolean condition) {
		if (condition) {
			fail("Condition is not false");
		}

	}

	public static void assertEquals(Object expected, Object actual) {
		if (expected == null && actual == null) {
			return;
		}

		if (expected != null && expected.equals(actual)) {
			return;
		}

		fail("Two arguments are not equal");

	}

	static public void fail(String message) {
		if (message == null) {
			throw new AssertionFailedError();
		}
		throw new AssertionFailedError(message);
	}
}
