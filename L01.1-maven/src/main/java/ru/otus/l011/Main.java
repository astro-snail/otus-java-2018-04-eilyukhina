package ru.otus.l011;

import org.apache.commons.collections4.CollectionUtils;

/*java -cp L01.1-maven.jar;C:\Users\Elena\.m2\repository\org\apache\commons\commons-collections4\4.1\commons-collections4-4.1.jar ru.otus.l01
1.Main */

public class Main {

	public static void main(String[] args) {

		Integer[] numbers = new Integer[Short.MAX_VALUE * 10];

		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = i;
		}

		System.out.println("First number: " + numbers[0]);

		CollectionUtils.reverseArray(numbers);

		System.out.println("First number reversed: " + numbers[0]);

	}

}
