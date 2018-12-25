package ru.otus.l151.app.messages;

import java.sql.SQLException;

import ru.otus.l151.app.MsgToDB;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.messagesystem.MessageException;

public class MsgDeleteUserRequest extends MsgToDB {

	private final UserDataSet user;

	public MsgDeleteUserRequest(Address from, Address to, MessageContext context, UserDataSet user) {
		super(from, to, context);
		this.user = user;
	}

	@Override
	public void exec(DBService dbService) throws MessageException {
		try {
			dbService.delete(user);
			dbService.getMessageSystem()
					.sendMessage(new MsgDeleteUserResponse(getTo(), getFrom(), getContext(), "User deleted"));
		} catch (SQLException e) {
			throw new MessageException(e);
		}
	}

}
