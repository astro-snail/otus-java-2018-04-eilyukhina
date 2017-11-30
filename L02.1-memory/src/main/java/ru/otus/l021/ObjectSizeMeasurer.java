package ru.otus.l021;

import java.util.function.Supplier;

public class ObjectSizeMeasurer {
	
	private static final long SLEEP_INTERVAL = 1_000;
	private static final int SAMPLE_SIZE = 1_000_000;

	public long getObjectSize(Supplier<?> obj) {
		
		Object[] tempArray = new Object[SAMPLE_SIZE];
		
	    long startMemory = getMemoryUsed();

		for (int i = 0; i < tempArray.length; i++) {
			tempArray[i] = obj.get();
		}

		long endMemory = getMemoryUsed();
		
		System.out.println("Array of size " + tempArray.length + " created");
		
		return (endMemory - startMemory) / SAMPLE_SIZE; 
	}
	
	private long getMemoryUsed() {
		Runtime runtime = Runtime.getRuntime();
		clearMemory();
		return runtime.totalMemory() - runtime.freeMemory();
	}
	
	public void clearMemory() {
		try {
			System.gc();
			Thread.sleep(SLEEP_INTERVAL);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
