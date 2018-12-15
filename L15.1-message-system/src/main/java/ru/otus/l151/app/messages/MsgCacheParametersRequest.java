package ru.otus.l151.app.messages;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.MsgToDB;
import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.messagesystem.Address;

public class MsgCacheParametersRequest extends MsgToDB {
	private final AsyncContext asyncContext;
	
	public MsgCacheParametersRequest(Address from, Address to, AsyncContext asyncContext) {
		super(from, to);
		this.asyncContext = asyncContext;
	}

	public void exec(DBService dbService) {
		dbService.getMessageSystem().sendMessage(new MsgCacheParametersResponse(getTo(), getFrom(), asyncContext, dbService.getCacheParameters()));
	}
}
