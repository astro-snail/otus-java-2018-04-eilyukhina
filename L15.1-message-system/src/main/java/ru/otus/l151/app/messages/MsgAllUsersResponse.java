package ru.otus.l151.app.messages;

import java.util.List;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.MsgToUI;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.uiservice.UIService;

public class MsgAllUsersResponse extends MsgToUI {
	private final List<UserDataSet> users;
	private final AsyncContext asyncContext;
	
	public MsgAllUsersResponse(Address from, Address to, AsyncContext asyncContext, List<UserDataSet> users) {
		super(from, to);
		this.users = users;
		this.asyncContext = asyncContext;
	}

	@Override
	public void exec(UIService uiService) {
		uiService.handleResponse(asyncContext, users);
	}

}
