package ru.otus.l151.uiservice;

import java.util.List;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.MessageSystemContext;
import ru.otus.l151.app.messages.*;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Message;
import ru.otus.l151.messagesystem.MessageSystem;

public class UIServiceImpl implements UIService {
	
	private final MessageSystemContext context;
	
	public UIServiceImpl(MessageSystemContext context) {
		this.context = context;
	}
	
	@Override
	public MessageSystem getMessageSystem() {
		return context.getMessageSystem();
	}
	
	@Override
	public void register() {
		context.registerUIService(this);		
	}
	
	@Override
	public void handleRequest(AsyncContext asyncContext, Long id) {
        Message message = new MsgUserRequestById(context.getUIServiceAddress(), context.getDBServiceAddress(), asyncContext, id);
        getMessageSystem().sendMessage(message);
    }
	
	@Override
	public void handleRequest(AsyncContext asyncContext, String name) {
		Message message = new MsgUserRequestByName(context.getUIServiceAddress(), context.getDBServiceAddress(), asyncContext, name);
        getMessageSystem().sendMessage(message);
	}

	@Override
	public void handleRequest(AsyncContext asyncContext) {
		Message message = new MsgAllUsersRequest(context.getUIServiceAddress(), context.getDBServiceAddress(), asyncContext);
        getMessageSystem().sendMessage(message);	
	}
	
	@Override
    public void handleRequest(AsyncContext asyncContext, Operation operation, UserDataSet user) {
		Message message = null;
		switch (operation) {
			case SAVE: 
				message = new MsgAddUserRequest(context.getUIServiceAddress(), context.getDBServiceAddress(), asyncContext, user);
				break;
			case DELETE: 
				message = new MsgDeleteUserRequest(context.getUIServiceAddress(), context.getDBServiceAddress(), asyncContext, user);
				break;
		}	
		if (message != null) {
			getMessageSystem().sendMessage(message);
		}	
    }

	@Override
	public void handleResponse(AsyncContext asyncContext, UserDataSet user) {
		asyncContext.getRequest().setAttribute("id", user.getId());
		asyncContext.getRequest().setAttribute("name", user.getName());
		asyncContext.getRequest().setAttribute("streetAddress", user.getAddress().getStreet());
		asyncContext.getRequest().setAttribute("phoneNumber", user.getPhones().get(0).getNumber());
	    asyncContext.dispatch("/user-info.jsp");		
	}

	@Override
	public void handleResponse(AsyncContext asyncContext, List<UserDataSet> users) {
		asyncContext.getRequest().setAttribute("users", users);
	    asyncContext.dispatch("/user-list.jsp");
	}

	@Override
	public void handleResponse(AsyncContext asyncContext, String message) {

	}
}
