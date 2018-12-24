package ru.otus.l161.message;

import java.io.Serializable;

import com.google.gson.Gson;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Address from;
	private Address to;
	private String messageType;
	private String payload;
	
	public Message() {
		
	}
	
	public Message(Address from, Address to, Object payload) {
		if (payload == null) {
			throw new IllegalArgumentException("Payload must not be null");
		}
		this.from = from;
		this.to = to; 
		this.messageType = payload.getClass().getName();
		this.payload = new Gson().toJson(payload);
    }
    
	public Address getFrom() {
		return from;
	}
	
	public Address getTo() {
		return to;
	}
	
	public Class<?> getMessageType() {
		try {
			return Class.forName(messageType);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Object getPayload() {
		return new Gson().fromJson(payload, getMessageType());
	}
	
	@Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
