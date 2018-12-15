package ru.otus.l151.app.messages;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.MsgToUI;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.uiservice.UIService;

public class MsgDeleteUserResponse extends MsgToUI {
	private final String message;
	private final AsyncContext asyncContext;
	
	public MsgDeleteUserResponse(Address from, Address to, AsyncContext asyncContext, String message) {
		super(from, to);
		this.message = message;
		this.asyncContext = asyncContext;
	}

	public void exec(UIService uiService) {
		uiService.handleUserResponse(asyncContext, message);
	}
}
