package ru.otus.l151.messagesystem;

public class MessageException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public MessageException() {
		super();
	}
	
	public MessageException(String message) {
		super(message);
	}
	
	public MessageException(Throwable cause) {
		super(cause);
	}

	public MessageException(String message, Throwable cause) {
		super(cause);
	}
}
