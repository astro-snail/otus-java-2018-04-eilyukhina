package ru.otus.l151.app.messages;

import ru.otus.l151.app.MsgToDB;
import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageContext;

public class MsgCacheParametersRequest extends MsgToDB {
	
	public MsgCacheParametersRequest(Address from, Address to, MessageContext context) {
		super(from, to, context);
	}

	public void exec(DBService dbService) {
		dbService.getMessageSystem().sendMessage(new MsgCacheParametersResponse(getTo(), getFrom(), getContext(), dbService.getCacheParameters()));
	}
}
