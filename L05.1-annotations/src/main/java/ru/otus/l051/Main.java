package ru.otus.l051;

import ru.otus.l051.tester.*;

public class Main {

	public static void main(String[] args) {

		try {
			Tester.runTestsForClass("ru.otus.l051.test.MyTestNoDefaultConstructor");

			Tester.runTestsForClass("ru.otus.l051.test.MyTest");

			Tester.runTestsForClass("ru.otus.l051.validations.AssertionFailedError");

			Tester.runTestsForPackage("ru.otus.l051.test");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
