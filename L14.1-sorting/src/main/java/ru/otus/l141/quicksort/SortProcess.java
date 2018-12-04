package ru.otus.l141.quicksort;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class SortProcess extends Thread {
	private final int id;
	private int[] numbers;
	private int[] low;
	private int[] high;
	
	private volatile boolean partitioned = false;
	private CyclicBarrier cb;
	private CountDownLatch latch;

	private SubGroup subGroup;
	private SortProcess partner;
	private int pivot;

	private SortProcessManager manager;

	public SortProcess(SortProcessManager manager, CyclicBarrier cb, int id, int[] numbers) {
		this.manager = manager;
		this.id = id;
		this.numbers = numbers;
		this.cb = cb;
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

	public void partition() {
		int index = 0;

		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] <= pivot) {
				swap(i, index);
				index++;
			}
		}
		
		low = Arrays.copyOfRange(numbers, 0, index);
		high = Arrays.copyOfRange(numbers, index, numbers.length);

		// Test output
		System.out.println("Process ID: " + getProcessId() + " " + subGroup.toString() + " pivot: " + pivot + " low / high " + Arrays.toString(low) + " / " + Arrays.toString(high));

		synchronized (this) {
			partitioned = true;
			notifyAll();
		}
	}

	public void setUpForIteration(SubGroup subGroup, SortProcess partner, int pivot, CountDownLatch latch) {
		this.subGroup = subGroup; // low or high partition
		this.partner = partner; // partner thread to exchange data
		this.pivot = pivot;
		this.latch = latch; // count down latch for start of a new iteration
	}

	public int partitionSequential(int from, int to) {
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

	public void quickSortSequential(int from, int to) {
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

	public synchronized int[] getData() {
		while (!partitioned) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		partitioned = false;
		return subGroup == SubGroup.LOW ? high : low;
	}

	public int getProcessId() {
		return id;
	}

	public int[] getNumbers() {
		return numbers;
	}

	public void setData(int[] data) {
		int[] base = subGroup == SubGroup.LOW ? low : high;

		int[] mergedNumbers = new int[base.length + data.length];

		System.arraycopy(base, 0, mergedNumbers, 0, base.length);
		System.arraycopy(data, 0, mergedNumbers, base.length, data.length);

		numbers = mergedNumbers;
	}

	private void swapData() {
		int[] data = partner.getData();
		setData(data);
	}

	@Override
	public void run() {
		quickSort(false);
	}
	
	private void await() {
		try {
			cb.await();
		} catch (InterruptedException e) {

		} catch (BrokenBarrierException e) {

		}
	}

	public void quickSort(boolean preSort) {
		
		if (preSort) {
			quickSortSequential(0, numbers.length);
		}
		
		while (!manager.isTerminated()) {
			manager.checkReady();
			
			if (manager.isTerminated()) break;
			
			partition();
			swapData();
			
			// Wait for all worker threads to finish current iteration
			await();
			
			// Count down to release the main thread for a new iteration
			latch.countDown();
		}
		
		quickSortSequential(0, numbers.length);
	}
}
