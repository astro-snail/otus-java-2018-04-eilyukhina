package ru.otus.l151.messagesystem;

import java.util.EventListener;

public interface MessageEventListener extends EventListener {
	
	public void messageReceived(MessageEvent event);

}
