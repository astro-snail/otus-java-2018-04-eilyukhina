package ru.otus.l051.test;

import ru.otus.l051.annotations.*;

public class MyAnotherTest {
	
	@Before
	public void beforeTest() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@After
	public void afterTest() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@Test (expected = NullPointerException.class)
	public void testShouldFail() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@Test
	public void testShouldPass() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
