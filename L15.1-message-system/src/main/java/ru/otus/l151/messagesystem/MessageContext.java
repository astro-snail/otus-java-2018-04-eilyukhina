package ru.otus.l151.messagesystem;

import java.util.ArrayList;
import java.util.List;

public class MessageContext {

	private List<MessageEventListener> listeners = new ArrayList<>();

	public void addListener(MessageEventListener listener) {
		listeners.add(listener);
	}

	public void complete(Object result) {
		for (MessageEventListener listener : listeners) {
			listener.messageReceived(new MessageEvent(this, result));
		}
	}

}
