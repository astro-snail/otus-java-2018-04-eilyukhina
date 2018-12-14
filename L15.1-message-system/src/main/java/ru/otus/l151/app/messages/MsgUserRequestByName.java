package ru.otus.l151.app.messages;

import java.sql.SQLException;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.MsgToDB;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageException;

public class MsgUserRequestByName extends MsgToDB {
	private final String name;
	private final AsyncContext asyncContext;
	
	public MsgUserRequestByName(Address from, Address to, AsyncContext asyncContext, String name) {
		super(from, to);
		this.name = name;
		this.asyncContext = asyncContext;
	}

	public void exec(DBService dbService) throws MessageException {
		try {
			UserDataSet user = dbService.loadByName(name);
			dbService.getMessageSystem().sendMessage(new MsgUserResponse(getTo(), getFrom(), asyncContext, user));
		} catch (SQLException e) {
			throw new MessageException(e);
		}
	}
}
