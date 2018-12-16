package ru.otus.l151.app.messages;

import java.util.List;

import ru.otus.l151.app.MsgToUI;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.uiservice.UIService;

public class MsgAllUsersResponse extends MsgToUI {
	
	private final List<UserDataSet> users;
	
	public MsgAllUsersResponse(Address from, Address to, MessageContext context, List<UserDataSet> users) {
		super(from, to, context);
		this.users = users;
	}

	@Override
	public void exec(UIService uiService) {
		uiService.handleUserResponse(getContext(), users);
	}

}
