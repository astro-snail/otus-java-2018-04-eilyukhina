package ru.otus.l151.app.messages;

import java.sql.SQLException;

import ru.otus.l151.app.MsgToDB;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.messagesystem.MessageException;

public class MsgUserRequestByName extends MsgToDB {
	
	private final String name;
	
	public MsgUserRequestByName(Address from, Address to, MessageContext context, String name) {
		super(from, to, context);
		this.name = name;
	}

	public void exec(DBService dbService) throws MessageException {
		try {
			UserDataSet user = dbService.loadByName(name);
			dbService.getMessageSystem().sendMessage(new MsgUserResponse(getTo(), getFrom(), getContext(), user));
		} catch (SQLException e) {
			throw new MessageException(e);
		}
	}
}
