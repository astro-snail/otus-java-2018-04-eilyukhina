package ru.otus.l151.app.messages;

import java.sql.SQLException;

import ru.otus.l151.app.MsgToDB;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.messagesystem.MessageException;

public class MsgUserRequestById extends MsgToDB {
	
	private final Long id;
	
	public MsgUserRequestById(Address from, Address to, MessageContext context, Long id) {
		super(from, to, context);
		this.id = id;
	}

	public void exec(DBService dbService) throws MessageException {
		try {
			UserDataSet user = dbService.load(id);
			dbService.getMessageSystem().sendMessage(new MsgUserResponse(getTo(), getFrom(), getContext(), user));
		} catch (SQLException e) {
			throw new MessageException(e);
		}
	}
}
