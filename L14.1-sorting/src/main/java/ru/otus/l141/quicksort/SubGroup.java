package ru.otus.l141.quicksort;

public enum SubGroup {

	LOW(0), HIGH(1);
	
	private int value;
	
	private SubGroup(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

}
