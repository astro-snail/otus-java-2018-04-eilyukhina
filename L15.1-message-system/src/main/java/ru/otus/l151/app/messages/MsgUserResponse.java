package ru.otus.l151.app.messages;

import ru.otus.l151.app.MsgToUI;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.uiservice.UIService;

public class MsgUserResponse extends MsgToUI {

	private final UserDataSet user;

	public MsgUserResponse(Address from, Address to, MessageContext context, UserDataSet user) {
		super(from, to, context);
		this.user = user;
	}

	@Override
	public void exec(UIService uiService) {
		uiService.handleUserResponse(getContext(), user);
	}

}
