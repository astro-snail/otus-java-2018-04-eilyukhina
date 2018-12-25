package ru.otus.l161.message;

import java.util.EventListener;

public interface MessageEventListener extends EventListener {

	public void messageReceived(MessageEvent event);

}
