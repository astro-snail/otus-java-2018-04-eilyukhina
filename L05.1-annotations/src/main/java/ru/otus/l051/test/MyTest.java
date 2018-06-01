package ru.otus.l051.test;

import ru.otus.l051.annotations.*;

public class MyTest {
	
	@Before
	public void beforeTest() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@After
	public void afterTest() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	@Test
	public void testSomething() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
	}

}
