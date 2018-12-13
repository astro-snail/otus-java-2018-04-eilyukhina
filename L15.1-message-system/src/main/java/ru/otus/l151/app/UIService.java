package ru.otus.l151.app;

import java.util.List;

import javax.servlet.AsyncContext;

import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Addressee;

public interface UIService extends Addressee {
	
	void requestUser(AsyncContext asyncContext, Long id);
	
	void requestUser(AsyncContext asyncContext, String name);
	
	void requestAllUsers(AsyncContext asyncContext);
	
	void saveUser(AsyncContext asyncContext, UserDataSet user);
	
	void receiveUser(AsyncContext asyncContext, UserDataSet user);
	
	void receiveAllUsers(AsyncContext asyncContext, List<UserDataSet> users);
	
}
