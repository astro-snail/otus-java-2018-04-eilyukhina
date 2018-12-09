package ru.otus.l141.quicksort;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.otus.l141.generator.DataGenerator;

public class SortProcessManager {
	private boolean shouldTerminate = false; 
	private boolean ready = false; 

	private final SortProcess[] processes;
	private int[] result = new int[0];
	
	private final static Logger logger = Logger.getLogger(SortProcessManager.class.getName());

	public SortProcessManager(int count, int from, int to) {
		this(count, new DataGenerator(from, to).getNumbers());
	}

	public SortProcessManager(int count, int[] unsorted) {
		
		logger.log(Level.INFO, "Unsorted: " + Arrays.toString(unsorted));
				
		int numberOfElements = unsorted.length;
		int numberPerProcess = numberOfElements / count;

		processes = new SortProcess[count];
		
		// Cyclic barrier to synchronise worker threads at the end of each iteration
		// Before releasing worker threads, execute setReady(false) to make them wait until the next iteration is ready
		CyclicBarrier cb = new CyclicBarrier(count, () -> setReady(false));

		// Split data among worker threads
		for (int i = 0; i < count; i++) {
			int indexFrom = i * numberPerProcess;
			int indexTo = (i + 1) * numberPerProcess;

			if (i == count - 1) {
				indexTo = numberOfElements;
			}
			processes[i] = new SortProcess(cb, () -> isReady(), Arrays.copyOfRange(unsorted, indexFrom, indexTo));
		}
	}
	
	private synchronized boolean isReady() {
		while (!(ready || shouldTerminate)) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		return shouldTerminate ? false : true;
	}

	private synchronized void setReady(boolean ready) {
		this.ready = ready;
		notifyAll();
	}
	
	private synchronized void setShouldTerminate(boolean shouldTerminate) {
		this.shouldTerminate = shouldTerminate;
		notifyAll();
	}

	public void run() {
		quickSort();
	}
	
	public int[] getResult() {
		return result;
	}

	private int choosePivot(int group, int size) {
		int pivot = -1;

		for (int i = 0; i < size && pivot == -1; i++) {
			pivot = processes[group * size + i].choosePivotRandom();
		}
		return pivot;
	}

	private static int[] combine(int[] a, int[] b) {
		int[] result = new int[a.length + b.length];
		
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		
		return result;
    }
	
	private synchronized void await() {
		while (ready) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
	}
	
	private void quickSort() {
		int processCount = processes.length;
		int groupSize = processes.length;

		for (SortProcess process : processes) {
			process.start();
		}

		while (groupSize > 1) {

			int numberOfGroups = processCount / groupSize;

			for (int i = 0; i < numberOfGroups; i++) {
				int pivot = choosePivot(i, groupSize);

				for (int j = 0; j < groupSize / 2; j++) {
					SortProcess low = processes[i * groupSize + j];
					SortProcess high = processes[i * groupSize + groupSize / 2 + j];

					low.setUpForIteration(SubGroup.LOW, high, pivot);
					high.setUpForIteration(SubGroup.HIGH, low, pivot);
				}
			}
			
			setReady(true);
			await();

			groupSize = groupSize / 2;
		}
		
		setShouldTerminate(true);

		// Combine all threads results
		for (SortProcess process : processes) {
			try {
				process.join();
				result = combine(result, process.getNumbers());
			} catch (InterruptedException e) {

			}
		}
		
		logger.log(Level.INFO, "Sorted: " + Arrays.toString(result));
	}

}
