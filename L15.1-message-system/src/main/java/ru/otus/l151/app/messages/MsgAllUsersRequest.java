package ru.otus.l151.app.messages;

import java.sql.SQLException;
import java.util.List;

import ru.otus.l151.app.MsgToDB;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.messagesystem.MessageException;

public class MsgAllUsersRequest extends MsgToDB {

	public MsgAllUsersRequest(Address from, Address to, MessageContext context) {
		super(from, to, context);
	}

	public void exec(DBService dbService) throws MessageException {
		try {
			List<UserDataSet> users = dbService.loadAll();
			dbService.getMessageSystem().sendMessage(new MsgAllUsersResponse(getTo(), getFrom(), getContext(), users));
		} catch (SQLException e) {
			throw new MessageException(e);
		}
	}
}
