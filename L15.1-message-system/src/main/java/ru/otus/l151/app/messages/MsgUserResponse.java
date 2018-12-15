package ru.otus.l151.app.messages;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.MsgToUI;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.uiservice.UIService;

public class MsgUserResponse extends MsgToUI {
	private final UserDataSet user;
	private final AsyncContext asyncContext;
	
	public MsgUserResponse(Address from, Address to, AsyncContext asyncContext, UserDataSet user) {
		super(from, to);
		this.user = user;
		this.asyncContext = asyncContext;
	}

	@Override
	public void exec(UIService uiService) {
		uiService.handleUserResponse(asyncContext, user);
	}

}
