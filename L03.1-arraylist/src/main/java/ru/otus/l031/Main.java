package ru.otus.l031;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		
		MyArrayList<String> myList = new MyArrayList<>();
		MyArrayList<String> myCopyList = new MyArrayList<>(10);
		
		System.out.println("Add elements to myList:");
		Collections.addAll(myList, "1a", "exb2", "3c");
		printList(myList);
		
		System.out.println("Add elements to myCopyList:");
		Collections.addAll(myCopyList, "10", "9", "8", "ahaha", "wow");
		printList(myCopyList);

		System.out.println("myList as array:");
		System.out.println(Arrays.toString(myList.toArray(new String[0])));
		
		System.out.println("myCopyList as array:");
		System.out.println(Arrays.toString(myCopyList.toArray(new String[0])));
		
		System.out.println("Copy myList to myCopyList:");
		Collections.copy(myCopyList, myList);
		printList(myCopyList);
		
		System.out.println("Sort myCopyList:");
		Collections.sort(myCopyList, String.CASE_INSENSITIVE_ORDER);
		printList(myCopyList);
	}

	private static void printList(List<?> list) {
		for (Object element : list) {
			System.out.println(element);
		}
	}
}
