package ru.otus.l021;

public class ObjectSizeMeasurer {
	
	private static final long SLEEP_INTERVAL = 1_000;
	private static final int SAMPLE_SIZE = 1_000_000;

	public long getObjectSize(ObjectCreator obj) {
		
		clearMemory();
		
	    long startMemory = getMemoryUsed();

		Object[] tempArray = new Object[SAMPLE_SIZE];
		
		for (int i = 0; i < tempArray.length; i++) {
			tempArray[i] = obj.create();
		}
		
		long endMemory = getMemoryUsed();
		
		clearMemory();
		
		return (endMemory - startMemory) / SAMPLE_SIZE; 
	}
	
	private long getMemoryUsed() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() - runtime.freeMemory();
	}
	
	private void clearMemory() {
		try {
			System.gc();
			Thread.sleep(SLEEP_INTERVAL);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
