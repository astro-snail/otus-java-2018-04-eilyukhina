package ru.otus.l151.messagesystem;

import java.util.EventObject;

public class MessageEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	private final Object value;

	public MessageEvent(MessageContext context, Object value) {
		super(context);
		this.value = value;
	}

	public MessageContext getMessageContext() {
		return (MessageContext) super.getSource();
	}

	public Object getValue() {
		return value;
	}

}
