package ru.otus.l141.quicksort;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import ru.otus.l141.generator.DataGenerator;

public class SortProcessManager extends Thread { // Can be started in a separate thread, but runs in main thread in this example

	private final SortProcess[] processes;
	private int[] result;

	private volatile boolean ready = false;
	private volatile boolean terminated = false;

	public SortProcessManager(int count, int from, int to) {
		this(count, new DataGenerator(from, to).getNumbers());
	}

	public SortProcessManager(int count, int[] unsorted) {
		
		System.out.println("Unsorted: " + Arrays.toString(unsorted));
				
		int numberOfElements = unsorted.length;
		int numberPerProcess = numberOfElements / count;

		processes = new SortProcess[count];
		
		// Cyclic barrier to synchronise worker threads at the end of each iteration
		// Before releasing worker threads, set ready = false to make them wait until next iteration is ready
		CyclicBarrier cb = new CyclicBarrier(count, () -> setReady(false));

		// Split data among worker threads
		for (int i = 0; i < count; i++) {
			int indexFrom = i * numberPerProcess;
			int indexTo = (i + 1) * numberPerProcess;

			if (i == count - 1) {
				indexTo = numberOfElements;
			}
			processes[i] = new SortProcess(this, cb, i, Arrays.copyOfRange(unsorted, indexFrom, indexTo));
		}
	}

	public synchronized void checkReady() {
		while (!ready) {
			if (!terminated) {
				try {
					wait();
				} catch (InterruptedException e) {

				}
			} else {
				return;
			}
		}
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	@Override
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

	private void quickSort() {
		int processCount = processes.length;
		int groupSize = processes.length;

		for (SortProcess process : processes) {
			process.start();
		}

		while (groupSize > 1) {

			int numberOfGroups = processCount / groupSize;

			CountDownLatch latch = new CountDownLatch(processCount);

			for (int i = 0; i < numberOfGroups; i++) {
				int pivot = choosePivot(i, groupSize);

				for (int j = 0; j < groupSize / 2; j++) {
					SortProcess low = processes[i * groupSize + j];
					SortProcess high = processes[i * groupSize + groupSize / 2 + j];

					low.setUpForIteration(SubGroup.LOW, high, pivot, latch);
					high.setUpForIteration(SubGroup.HIGH, low, pivot, latch);
				}
			}

			synchronized (this) {
				setReady(true);
				notifyAll();
			}

			try {
				// Wait until all worker threads finish current iteration
				latch.await();
			} catch (InterruptedException e) {

			}

			groupSize = groupSize / 2;
		}

		synchronized (this)	{
			setTerminated(true);
			notifyAll();
		}

		// Combine all threads results
		result = new int[0];
		for (SortProcess process : processes) {
			try {
				process.join();
				result = combine(result, process.getNumbers());
			} catch (InterruptedException e) {

			}
		}
		
		System.out.println("Sorted: " + Arrays.toString(result));
	}

}
