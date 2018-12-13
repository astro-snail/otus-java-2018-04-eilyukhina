package ru.otus.l151.app.messages;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.DBService;
import ru.otus.l151.app.MsgToDB;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageException;

public class MsgAllUsersRequest extends MsgToDB {
	private final AsyncContext asyncContext;
	
	public MsgAllUsersRequest(Address from, Address to, AsyncContext asyncContext) {
		super(from, to);
		this.asyncContext = asyncContext;
		
	}

	public void exec(DBService dbService) throws MessageException {
		try {
			List<UserDataSet> users = dbService.loadAll();
			dbService.getMessageSystem().sendMessage(new MsgAllUsersResponse(getTo(), getFrom(), asyncContext, users));
		} catch (SQLException e) {
			throw new MessageException(e);
		}
	}
}
