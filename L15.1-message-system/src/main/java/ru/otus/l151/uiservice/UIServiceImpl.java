package ru.otus.l151.uiservice;

import java.util.List;
import java.util.Map;

import ru.otus.l151.app.MessageSystemContext;
import ru.otus.l151.app.messages.*;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.Message;
import ru.otus.l151.messagesystem.MessageContext;
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
	public void handleUserRequest(MessageContext messageContext, Long id) {
		Message message = new MsgUserRequestById(getAddress(), context.getDBServiceAddress(), messageContext, id);
        getMessageSystem().sendMessage(message);		
	}
	
	@Override
	public void handleUserRequest(MessageContext messageContext, String name) {
		Message message = new MsgUserRequestByName(getAddress(), context.getDBServiceAddress(), messageContext, name);
        getMessageSystem().sendMessage(message);
	}

	@Override
	public void handleUserRequest(MessageContext messageContext) {
		Message message = new MsgAllUsersRequest(getAddress(), context.getDBServiceAddress(), messageContext);
        getMessageSystem().sendMessage(message);
		
	}

	@Override
    public void handleUserRequest(MessageContext messageContext, Operation operation, UserDataSet user) {
		Message message = null;
		switch (operation) {
			case SELECT:
				message = new MsgUserRequestById(getAddress(), context.getDBServiceAddress(), messageContext, user.getId());
				break;
			case SAVE: 
				message = new MsgSaveUserRequest(getAddress(), context.getDBServiceAddress(), messageContext, user);
				break;
			case DELETE: 
				message = new MsgDeleteUserRequest(getAddress(), context.getDBServiceAddress(), messageContext, user);
				break;
		}	
		if (message != null) {
			getMessageSystem().sendMessage(message);
		}	
    }

	@Override
	public void handleUserResponse(MessageContext messageContext, UserDataSet user) {
		messageContext.complete(user);		
	}

	@Override
	public void handleUserResponse(MessageContext messageContext, List<UserDataSet> users) {
		messageContext.complete(users);
	}

	@Override
	public void handleUserResponse(MessageContext messageContext, String message) {
	    messageContext.complete(message);
	}
	
	@Override
	public void handleCacheRequest(MessageContext messageContext) {
		Message message = new MsgCacheParametersRequest(getAddress(), context.getDBServiceAddress(), messageContext);
        getMessageSystem().sendMessage(message);		
	}

	@Override
	public void handleCacheResponse(MessageContext messageContext, Map<String, String> cacheParameters) {
	    messageContext.complete(cacheParameters);		
	}
	
	@Override
	public void shutdown() {
		
	}
	
}
