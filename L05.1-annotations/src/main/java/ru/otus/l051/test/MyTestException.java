package ru.otus.l051.test;

import ru.otus.l051.annotations.*;
import static ru.otus.l051.tester.Assert.*;

public class MyTestException {
	
	@Before
	public void beforeTest() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@After
	public void afterTest() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@Test (expected = NullPointerException.class)
	public void testExceptionExpectedAndRaised() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
		throw new NullPointerException();
	}
	
	@Test (expected = NullPointerException.class)
	public void testExceptionExpectedButAnotherRaised() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
		throw new ArithmeticException();
	}
	
	@Test (expected = NullPointerException.class)
	public void testExceptionExpectedButNotRaised() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@Test
	public void testExceptionNotExpectedNotRaised() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
		assertTrue(true);
	}

	@Test
	public void testExceptionNotExpectedButRaised() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
		assertEquals(1, 2);
	}
}
