package ru.otus.l151.uiservice;

import java.util.List;
import java.util.Map;

import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Addressee;
import ru.otus.l151.messagesystem.MessageContext;

public interface UIService extends Addressee {
	
	void init();
	
	void handleUserRequest(MessageContext context, Long id);
	
	void handleUserRequest(MessageContext context, String name);
	
	void handleUserRequest(MessageContext context);
	
	void handleUserRequest(MessageContext context, Operation operation, UserDataSet user);
	
	void handleUserResponse(MessageContext context, UserDataSet user);
	
	void handleUserResponse(MessageContext context, List<UserDataSet> users);
	
	void handleUserResponse(MessageContext context, String message);
	
	void handleCacheRequest(MessageContext context);
	
	void handleCacheResponse(MessageContext context, Map<String, String> cacheParameters);
	
	void shutdown();
	
}
