package ru.otus.l161.message;

import java.util.EventObject;

public class MessageEvent extends EventObject {
	
	private static final long serialVersionUID = 1L;
	
	private final Object value;

	public MessageEvent(Message message, Object value) {
		super(message);		
		this.value = value;
	}
	
	public Message getMessage() {
		return (Message) super.getSource();
	}
	
	public Object getValue() {
		return value;
	}
	
}
