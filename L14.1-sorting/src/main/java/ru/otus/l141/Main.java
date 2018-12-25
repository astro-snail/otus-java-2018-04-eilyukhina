package ru.otus.l141;

import java.util.Scanner;

import ru.otus.l141.quicksort.SortProcessManager;

public class Main {
	// Number of worker threads must be power of 2
	private static final int NUMBER_OF_THREADS = 4;

	private static int getNumberOfElements() {
		int numberOfElements;

		try (Scanner scanner = new Scanner(System.in)) {
			numberOfElements = scanner.nextInt();
		}
		return numberOfElements;
	}

	private static void doSort(int numberOfElements) {
		SortProcessManager manager = new SortProcessManager(NUMBER_OF_THREADS, 0, numberOfElements);
		manager.run();
	}

	public static void main(String[] args) {
		doSort(getNumberOfElements());
	}
}
