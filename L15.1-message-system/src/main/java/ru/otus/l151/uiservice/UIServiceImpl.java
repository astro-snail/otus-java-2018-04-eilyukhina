package ru.otus.l151.uiservice;

import java.util.List;
import java.util.Map;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.MessageSystemContext;
import ru.otus.l151.app.messages.*;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.Message;
import ru.otus.l151.messagesystem.MessageSystem;

public class UIServiceImpl implements UIService {
	
	private final MessageSystemContext context;
	
	public UIServiceImpl(MessageSystemContext context) {
		this.context = context;
	}
	
	@Override
	public Address getAddress() {
		return context.getUIServiceAddress();
	}
	
	@Override
	public MessageSystem getMessageSystem() {
		return context.getMessageSystem();
	}
	
	@Override
	public void init() {
		context.registerUIService(this);		
	}
	
	@Override
	public void handleUserRequest(AsyncContext asyncContext, Long id) {
        Message message = new MsgUserRequestById(getAddress(), context.getDBServiceAddress(), asyncContext, id);
        getMessageSystem().sendMessage(message);
    }
	
	@Override
	public void handleUserRequest(AsyncContext asyncContext, String name) {
		Message message = new MsgUserRequestByName(getAddress(), context.getDBServiceAddress(), asyncContext, name);
        getMessageSystem().sendMessage(message);
	}

	@Override
	public void handleUserRequest(AsyncContext asyncContext) {
		Message message = new MsgAllUsersRequest(getAddress(), context.getDBServiceAddress(), asyncContext);
        getMessageSystem().sendMessage(message);	
	}
	
	@Override
    public void handleUserRequest(AsyncContext asyncContext, Operation operation, UserDataSet user) {
		Message message = null;
		switch (operation) {
			case SELECT:
				message = new MsgUserRequestById(getAddress(), context.getDBServiceAddress(), asyncContext, user.getId());
				break;
			case SAVE: 
				message = new MsgSaveUserRequest(getAddress(), context.getDBServiceAddress(), asyncContext, user);
				break;
			case DELETE: 
				message = new MsgDeleteUserRequest(getAddress(), context.getDBServiceAddress(), asyncContext, user);
				break;
		}	
		if (message != null) {
			getMessageSystem().sendMessage(message);
		}	
    }

	@Override
	public void handleUserResponse(AsyncContext asyncContext, UserDataSet user) {
		asyncContext.getRequest().setAttribute("user", user);
		asyncContext.complete();		
	}

	@Override
	public void handleUserResponse(AsyncContext asyncContext, List<UserDataSet> users) {
		asyncContext.getRequest().setAttribute("users", users);
	    asyncContext.complete();
	}

	@Override
	public void handleUserResponse(AsyncContext asyncContext, String message) {
		asyncContext.getRequest().setAttribute("message", message);
	    asyncContext.complete();
	}
	
	@Override
	public void handleCacheRequest(AsyncContext asyncContext) {
		Message message = new MsgCacheParametersRequest(getAddress(), context.getDBServiceAddress(), asyncContext);
        getMessageSystem().sendMessage(message);		
	}

	@Override
	public void handleCacheResponse(AsyncContext asyncContext, Map<String, String> cacheParameters) {
		asyncContext.getRequest().setAttribute("cacheParameters", cacheParameters);
	    asyncContext.complete();		
	}
	
	@Override
	public void shutdown() {
		
	}
	
}
