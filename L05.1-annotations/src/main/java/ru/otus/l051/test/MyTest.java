package ru.otus.l051.test;

import ru.otus.l051.annotations.*;
import static ru.otus.l051.tester.Assert.*;

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
	public void testShouldFail() {
		System.out.println(getClass().getName() + ' ' + Thread.currentThread().getStackTrace()[1].getMethodName());
		assertTrue(false);
	}

}
