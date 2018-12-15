package ru.otus.l151.uiservice;

import java.util.List;
import java.util.Map;

import javax.servlet.AsyncContext;

import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Addressee;

public interface UIService extends Addressee {
	
	void init();
	
	void handleUserRequest(AsyncContext asyncContext, Long id);
	
	void handleUserRequest(AsyncContext asyncContext, String name);
	
	void handleUserRequest(AsyncContext asyncContext);
	
	void handleUserRequest(AsyncContext asyncContext, Operation operation, UserDataSet user);
	
	void handleUserResponse(AsyncContext asyncContext, UserDataSet user);
	
	void handleUserResponse(AsyncContext asyncContext, List<UserDataSet> users);
	
	void handleUserResponse(AsyncContext asyncContext, String message);
	
	void handleCacheRequest(AsyncContext asyncContext);
	
	void handleCacheResponse(AsyncContext asyncContext, Map<String, String> cacheParameters);
	
	void shutdown();
	
}
