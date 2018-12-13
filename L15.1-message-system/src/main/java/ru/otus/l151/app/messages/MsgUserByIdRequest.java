package ru.otus.l151.app.messages;

import java.sql.SQLException;

import javax.servlet.AsyncContext;

import ru.otus.l151.app.DBService;
import ru.otus.l151.app.MsgToDB;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageException;

public class MsgUserByIdRequest extends MsgToDB {
	private final Long id;
	private final AsyncContext asyncContext;
	
	public MsgUserByIdRequest(Address from, Address to, AsyncContext asyncContext, Long id) {
		super(from, to);
		this.id = id;
		this.asyncContext = asyncContext;
	}

	public void exec(DBService dbService) throws MessageException {
		try {
			UserDataSet user = dbService.load(id);
			dbService.getMessageSystem().sendMessage(new MsgUserResponse(getTo(), getFrom(), asyncContext, user));
		} catch (SQLException e) {
			throw new MessageException(e);
		}
	}
}
