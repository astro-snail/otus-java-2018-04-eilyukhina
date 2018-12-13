package ru.otus.l151.servlet;

import java.util.List;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.MessageSystemContext;
import ru.otus.l151.app.UIService;
import ru.otus.l151.app.messages.*;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Message;
import ru.otus.l151.messagesystem.MessageSystem;

public class UIServiceImpl implements UIService {
	
	private final MessageSystemContext context;
	
	public UIServiceImpl(MessageSystemContext context) {
		this.context = context;
		register();
	}
	
	public void register() {
    	context.registerUIService(this);
    }
	
	@Override
	public MessageSystem getMessageSystem() {
		return context.getMessageSystem();
	}
	
	@Override
	public void requestUser(AsyncContext asyncContext, Long id) {
        Message message = new MsgUserByIdRequest(context.getUIServiceAddress(), context.getDBServiceAddress(), asyncContext, id);
        getMessageSystem().sendMessage(message);
    }
	
	@Override
	public void requestUser(AsyncContext asyncContext, String name) {
		Message message = new MsgUserByNameRequest(context.getUIServiceAddress(), context.getDBServiceAddress(), asyncContext, name);
        getMessageSystem().sendMessage(message);
	}

	@Override
	public void requestAllUsers(AsyncContext asyncContext) {
		Message message = new MsgAllUsersRequest(context.getUIServiceAddress(), context.getDBServiceAddress(), asyncContext);
        getMessageSystem().sendMessage(message);	
	}
	
	@Override
    public void saveUser(AsyncContext asyncContext, UserDataSet user) {
		Message message = new MsgAddUserRequest(context.getUIServiceAddress(), context.getDBServiceAddress(), asyncContext, user);
        getMessageSystem().sendMessage(message);
    }

	@Override
	public void receiveUser(AsyncContext asyncContext, UserDataSet user) {
		asyncContext.getRequest().setAttribute("id", user.getId());
		asyncContext.getRequest().setAttribute("name", user.getName());
		asyncContext.getRequest().setAttribute("streetAddress", user.getAddress().getStreet());
		asyncContext.getRequest().setAttribute("phoneNumber", user.getPhones().get(0).getNumber());
	    asyncContext.dispatch("/user-info.jsp");		
	}

	@Override
	public void receiveAllUsers(AsyncContext asyncContext, List<UserDataSet> users) {
		asyncContext.getRequest().setAttribute("users", users);
	    asyncContext.dispatch("/user-list.jsp");
	}
}
