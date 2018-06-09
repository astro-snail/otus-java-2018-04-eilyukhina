package ru.otus.l051.test;

import ru.otus.l051.annotations.*;

public class MyTestNoDefaultConstructor {
	
	private String message;
	
	public MyTestNoDefaultConstructor(String message) {
		this.message = message;
	}
	
	@Before
	public void beforeTest() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@After
	public void afterTest() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@Test
	public void testShouldNotRun() {
		System.out.println(message);
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
		throw new RuntimeException("Exception in test method");
	}
}
