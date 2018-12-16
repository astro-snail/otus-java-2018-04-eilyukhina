package ru.otus.l151.app.messages;

import ru.otus.l151.app.MsgToUI;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.uiservice.UIService;

public class MsgDeleteUserResponse extends MsgToUI {
	
	private final String message;
	
	public MsgDeleteUserResponse(Address from, Address to, MessageContext context, String message) {
		super(from, to, context);
		this.message = message;
	}

	public void exec(UIService uiService) {
		uiService.handleUserResponse(getContext(), message);
	}
}
