package ru.otus.l141.quicksort;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SortProcess extends Thread {
	private int[] numbers; // cannot make final since it changes during merge
	private int[] low;
	private int[] high;
	private SubGroup subGroup;
	private SortProcess partner;
	private int pivot;

	private volatile boolean partitioned = false;

	private final CyclicBarrier cb;
	private final Callable<Boolean> callback;

	private final static Logger logger = Logger.getLogger(SortProcess.class.getName());

	public SortProcess(CyclicBarrier cb, Callable<Boolean> callback, int[] numbers) {
		this.cb = cb;
		this.callback = callback;
		setNumbers(numbers);
	}

	public int choosePivotSorted() {
		if (numbers == null || numbers.length == 0) {
			return -1;
		}
		return numbers[numbers.length / 2];
	}

	public int choosePivotRandom() {
		if (numbers == null || numbers.length == 0) {
			return -1;
		}
		Random random = ThreadLocalRandom.current();
		return numbers[random.nextInt(numbers.length)];
	}

	private void partition() {
		int index = 0;

		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] <= pivot) {
				swap(i, index);
				index++;
			}
		}

		low = Arrays.copyOfRange(numbers, 0, index);
		high = Arrays.copyOfRange(numbers, index, numbers.length);

		logger.log(Level.INFO, "Thread ID: " + Thread.currentThread().getId() + " " + subGroup.toString() + " pivot: "
				+ pivot + " low / high " + Arrays.toString(low) + " / " + Arrays.toString(high));

		partitioned = true;

		synchronized (this) {
			notifyAll();
		}
	}

	public void setUpForIteration(SubGroup subGroup, SortProcess partner, int pivot) {
		// Low or high partition
		this.subGroup = subGroup;
		// Partner thread to exchange data with
		this.partner = partner;
		// Pivot for partitioning
		this.pivot = pivot;
	}

	private int partitionSequential(int from, int to) {
		int pivot = numbers[from];
		int index = from;

		for (int i = from + 1; i < to; i++) {
			if (numbers[i] <= pivot) {
				index++;
				swap(i, index);
			}
		}
		swap(from, index);

		return index;
	}

	private void quickSortSequential(int from, int to) {
		if (to - from <= 1) {
			return;
		}
		int index = partitionSequential(from, to);
		quickSortSequential(from, index);
		quickSortSequential(index + 1, to);
	}

	private void swap(int i, int j) {
		int temp = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
	}

	private int[] getData() {
		synchronized (this) {
			while (!partitioned) {
				try {
					wait();
				} catch (InterruptedException e) {

				}
			}
		}
		partitioned = false;
		return subGroup == SubGroup.LOW ? high : low;
	}

	public int[] getNumbers() {
		return numbers;
	}

	private void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}

	private void setData(int[] data) {
		int[] base = subGroup == SubGroup.LOW ? low : high;

		int[] mergedNumbers = new int[base.length + data.length];

		System.arraycopy(base, 0, mergedNumbers, 0, base.length);
		System.arraycopy(data, 0, mergedNumbers, base.length, data.length);

		setNumbers(mergedNumbers);
	}

	private void merge() {
		setData(partner.getData());
	}

	@Override
	public void run() {
		try {
			quickSort(false);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Wait for all worker threads to finish current iteration
	 */
	private void await() {
		try {
			cb.await();
		} catch (InterruptedException e) {

		} catch (BrokenBarrierException e) {

		}
	}

	private void quickSort(boolean preSort) throws Exception {
		if (preSort) {
			quickSortSequential(0, numbers.length);
		}

		while (callback.call()) {
			partition();
			merge();
			await();
		}

		quickSortSequential(0, numbers.length);
	}
}
