package ru.otus.l151.uiservice;

import java.util.List;

import javax.servlet.AsyncContext;

import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Addressee;

public interface UIService extends Addressee {
	
	void handleRequest(AsyncContext asyncContext, Long id);
	
	void handleRequest(AsyncContext asyncContext, String name);
	
	void handleRequest(AsyncContext asyncContext);
	
	void handleRequest(AsyncContext asyncContext, Operation operation, UserDataSet user);
	
	void handleResponse(AsyncContext asyncContext, UserDataSet user);
	
	void handleResponse(AsyncContext asyncContext, List<UserDataSet> users);
	
	void handleResponse(AsyncContext asyncContext, String message);
	
}
