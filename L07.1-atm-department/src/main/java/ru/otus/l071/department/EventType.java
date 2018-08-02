package ru.otus.l071.department;

public enum EventType {
	
	ADD ("ADD"),
	REMOVE ("REMOVE"),
	RESTORE ("RESTORE");

	private String event;
	
	private EventType(String event) {
		this.event = event;
	}
	
	public String getEvent() {
		return this.event;
	}
}
