package ru.otus.l051.tester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import ru.otus.l051.annotations.After;
import ru.otus.l051.annotations.Before;
import ru.otus.l051.annotations.Test;
import ru.otus.l051.validations.ExpectedExceptionError;

public class TestCase {
	
	private final TestClass testClass;
	private final Method testMethod;
	private String testResult;
	private Object obj;
	
	public TestCase(TestClass testClass, Method testMethod) {
		this.testClass = testClass;
		this.testMethod = testMethod;
		this.testResult = TestResult.NOT_RUN.getMessage();
	}
	
	public Method getTestMethod( ) {
		return testMethod;
	}
	
	public String getTestResult() {
		return testResult;
	}
	
	private void success() {
		testResult = TestResult.SUCCESS.getMessage();
	}
	
	private void fail(String message) {
		testResult = TestResult.FAILURE.getMessage() + " " + message;
	}
	
	private static void invokeInstanceMethod(Object obj, Method method) throws Throwable {
		Class<?>[] paramTypes = method.getParameterTypes();
		Object[] params = new Object[paramTypes.length];
		
		method.setAccessible(true);
		method.invoke(obj, params);
	}

	private void validateException(Throwable targetException) throws Throwable{
		Test annotation = testMethod.getAnnotation(Test.class);
		
		if (annotation.expected() == Test.None.class && targetException != null) {
			throw targetException;
		}
		
		if (annotation.expected() != Test.None.class) {
			if (targetException == null) {
				throw new ExpectedExceptionError(annotation.expected().getName() + " was expected but none was raised");
			}	
			
			if (targetException.getClass() != annotation.expected()) {
				throw new ExpectedExceptionError(annotation.expected().getName() + " was expected but " + targetException.getClass().getName() + " was raised");
			}	
		}
	}
	
	private void setUp() throws Throwable {
		
		List<Method> methodsBefore = testClass.getAnnotatedMethods(Before.class);
		
		for (Method each : methodsBefore) {
			invokeInstanceMethod(obj, each);
		}
	}
	
	private void tearDown() throws Throwable {

		List<Method> methodsAfter = testClass.getAnnotatedMethods(After.class);
		
		for (Method each : methodsAfter) {
			invokeInstanceMethod(obj, each);
		}
	}
	
	private void runTest() throws Throwable {
		Throwable targetException = null;
		
		try {
			invokeInstanceMethod(obj, testMethod);
		} catch (InvocationTargetException e) {
			targetException = e.getCause();
		} finally {
			validateException(targetException);
		}
	}
	
	private void run() throws Throwable {
		Throwable exception = null;
		
		obj = testClass.getConstructor().newInstance();
		
		setUp();
		
		try {
			runTest();
		} catch (Throwable running) {
			exception = running;
		} finally {
			try {
				tearDown();
			} catch(Throwable tearingDown) {
				if (exception == null) {
					exception = tearingDown;
				}
			}
			obj = null;
		}
		
		if (exception != null) {
			throw exception;
		}
	}	
	
	public void doTest() {			
		try {
			run();
			success();
		} catch(Throwable e) {
			fail(e.getMessage());
		}	
	}	
}
