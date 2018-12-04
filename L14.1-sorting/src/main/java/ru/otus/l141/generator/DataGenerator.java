package ru.otus.l141.generator;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {
	private int[] numbers;

	public DataGenerator(final int from, final int to) {

		numbers = new int[to - from];

		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = from + i;
		}

		Random random = ThreadLocalRandom.current();

		// Shuffle array
		for (int i = numbers.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			int temp = numbers[index];
			numbers[index] = numbers[i];
			numbers[i] = temp;
		}
	}

	public int[] getNumbers() {
		return numbers;
	}

	@Override
	public String toString() {
		return Arrays.toString(numbers);
	}
}
