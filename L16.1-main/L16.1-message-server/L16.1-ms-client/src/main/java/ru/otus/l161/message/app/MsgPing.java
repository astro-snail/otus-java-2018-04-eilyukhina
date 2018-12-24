package ru.otus.l161.message.app;

import com.google.gson.Gson;

public class MsgPing {
	
	private final long time;
	
	public MsgPing() {
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
