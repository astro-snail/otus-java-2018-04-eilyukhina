package ru.otus.l051.tester;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.otus.l051.annotations.*;

public class Tester {
	
	private final TestClass testClass;
	private List<TestCase> testCases = new ArrayList<>();
	
	private Tester(Class<?> testClass) {
		this.testClass = new TestClass(testClass);
		createTestCases();
	}
	
	private void createTestCases() {
		for (Method testMethod : testClass.getAnnotatedMethods(Test.class)) {
			testCases.add(new TestCase(testClass, testMethod));
		}
	}
	
	private void runTests() {
		int count = 0;
		
		System.out.println("Class: " + testClass.getTestClass().getName());
		System.out.println("Number of test cases: " + testCases.size());
		
		for (TestCase testCase : testCases) {		
			System.out.println("Test case: " + ++count);
			System.out.println("Method: " + testCase.getTestMethod().getName());
			
			testCase.doTest();
            
			System.out.println(testCase.getTestResult());
			System.out.println();
		}	
	}
	
	public static void runTestsForClass(String className) throws ClassNotFoundException {
		Tester tester = new Tester(Class.forName(className));
		tester.runTests();		
	}
	
	public static void runTestsForPackage(String packageName) throws ClassNotFoundException {
		for (String className : getClassesInPackage(packageName)) {
			runTestsForClass(className);
		}
	}
	
	private static List<String> getClassesInPackage(String packageName) {
		/**
		 * Credit: https://dzone.com/articles/get-all-classes-within-package
		 */
		String path = packageName.replace('.', '/');
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource(path);
		File directory = new File(resource.getFile());
		
		return findClassesInDirectory(directory, packageName);
	}
	
	private static List<String> findClassesInDirectory(File directory, String packageName) {
		/**
		 * Credit: https://dzone.com/articles/get-all-classes-within-package
		 */
		List<String> classNames = new ArrayList<>();
		
		if (!directory.exists()) {
			return classNames;
		}
		
		File[] files = directory.listFiles();
		
		for (File file : files) {
			if (file.isDirectory()) {
				classNames.addAll(findClassesInDirectory(file, packageName + '.' + file.getName()));
			} else if (file.getName().endsWith(".class")){
				String className = packageName + '.' + file.getName().substring(0, file.getName().lastIndexOf('.'));
				classNames.add(className);
			}
		}
		
		return classNames;
	}
}
