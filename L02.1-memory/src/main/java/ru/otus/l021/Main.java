package ru.otus.l021;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  VM options -Xmx512m -Xms512m -XX:+UseCompressedOops
 */
public class Main {

	public static void main(String[] args) {

		System.out.println("pid: " + ManagementFactory.getRuntimeMXBean().getName());

		ObjectSizeMeasurer measurer = new ObjectSizeMeasurer();
        
		// For some reason without clearing memory up front the first measurement is always lower that it should be.
		measurer.clearMemory(); 

		System.out.println("Size of null: " + measurer.getObjectSize(() -> null));
		System.out.println("Size of Object: " + measurer.getObjectSize(() -> new Object()));
		System.out.println("Size of Integer: " + measurer.getObjectSize(() -> new Integer(5)));
		System.out.println("Size of Long: " + measurer.getObjectSize(() -> new Long(5L)));
		System.out.println("Size of Float: " + measurer.getObjectSize(() -> new Float(5.0f)));
		System.out.println("Size of Double: " + measurer.getObjectSize(() -> new Double(5.0)));
		System.out.println("Size of String: " + measurer.getObjectSize(() -> new String("")));
		System.out.println("Size of String char[0]: " + measurer.getObjectSize(() -> new String(new char[0])));
		System.out.println("Size of String abc: " + measurer.getObjectSize(() -> new String("abc")));
		System.out.println("Size of String char[3] abc: " + measurer.getObjectSize(() -> new String(new char[] {'a', 'b', 'c'})));
		System.out.println("Size of int[0]: " + measurer.getObjectSize(() -> new int[0]));
		System.out.println("Size of int[10]: " + measurer.getObjectSize(() -> new int[10]));
		System.out.println("Size of int[100]: " + measurer.getObjectSize(() -> new int[100]));
		System.out.println("Size of ArrayList<Integer>(): " + measurer.getObjectSize(() -> new ArrayList<Integer>()));
		System.out.println("Size of ArrayList<Integer>(10): " + measurer.getObjectSize(() -> new ArrayList<Integer>(Arrays.asList(new Integer[10]))));
		System.out.println("Size of MyClass: " + measurer.getObjectSize(() -> new MyClass()));
		System.out.println("Size of ObjectSizeMeasurer: " + measurer.getObjectSize(() -> new ObjectSizeMeasurer()));
	}
}	

class MyClass {
	private int i = 0;
	private long l = 1;
	private char[] c = new char[100];
}


