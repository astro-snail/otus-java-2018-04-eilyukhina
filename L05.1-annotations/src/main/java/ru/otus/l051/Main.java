package ru.otus.l051;

import ru.otus.l051.tester.*;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		
		try {
			String className = "ru.otus.l051.test.MyTest";
			System.out.println("Run annotated methods of class " + className);
			Tester.runTestsForClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		
		try {
			String packageName = "ru.otus.l051.test";
			System.out.println("Run annotated methods of classes in package " + packageName);
		    Tester.runTestsForPackage(packageName);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}
