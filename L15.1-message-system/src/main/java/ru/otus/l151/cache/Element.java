package ru.otus.l151.cache;

public class Element {
	
	private final Object key;
	private final Object value;
	private final long creationTime;
	private long lastAccessTime;
	
	public Element(Object key, Object value) {
		this.key = key;
		this.value = value;
		this.creationTime = getCurrentTime();		
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public long getCreationTime() {
		return creationTime;
	}
	
	@Override
	public String toString() {
		return key + ": " + value + ", created: " + creationTime + ", last accessed: " + lastAccessTime; 
	}
	
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}
}
