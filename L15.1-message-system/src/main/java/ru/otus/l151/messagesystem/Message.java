package ru.otus.l151.messagesystem;

public abstract class Message {
	
	private final Address from;
	private final Address to;
	private final MessageContext context;
	
	public Message(Address from, Address to, MessageContext context) {
		this.from = from;
		this.to = to; 
		this.context = context;
	}
	
	public Address getFrom() {
		return from;
	}
	
	public Address getTo() {
		return to;
	}
	
	public MessageContext getContext() {
		return context;
	}
	
	public abstract void exec(Addressee addressee) throws MessageException;

}
