package ru.otus.l161.uiservice;

import ru.otus.l161.dbmessages.Request;
import ru.otus.l161.message.Message;
import ru.otus.l161.message.MessageEventListener;

public interface UIService {

	void init();

	void doRequest(Request request, MessageEventListener listener);

	void handleResponse(Message message);

	void shutdown();

}
