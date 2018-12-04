package ru.otus.l141.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.l141.quicksort.SortProcessManager;

class TestSort {
	private static final int NUMBER_OF_THREADS = 4;
	private static final int NUMBER_OF_ELEMENTS = 50;
	
	private SortProcessManager manager;
	private int[] expected;
	
	@BeforeEach
	void setUp() throws Exception {
		manager = new SortProcessManager(NUMBER_OF_THREADS, 0, NUMBER_OF_ELEMENTS);
		
		expected = new int[NUMBER_OF_ELEMENTS];
		for (int i = 0; i < expected.length; i++) {
			expected[i] = i;
		}
	}

	@AfterEach
	void tearDown() throws Exception {
		manager = null;
		expected = null;
	}

	@Test
	void testRun() {
		manager.run();
		assertArrayEquals(expected, manager.getResult());
	}

}
