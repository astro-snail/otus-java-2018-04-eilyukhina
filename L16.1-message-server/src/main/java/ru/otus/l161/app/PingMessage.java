package ru.otus.l161.app;

import com.google.gson.Gson;

public class PingMessage {
	
	private final long time;
	
	public PingMessage() {
		this.time = System.currentTimeMillis();
	}

    public long getTime() {
        return time;
    }
    
    @Override
    public String toString() {
    	return new Gson().toJson(this);
    }
}
