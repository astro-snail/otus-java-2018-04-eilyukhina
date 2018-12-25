package ru.otus.l151.app;

import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.Addressee;
import ru.otus.l151.messagesystem.Message;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.messagesystem.MessageException;

public abstract class MsgToDB extends Message {

	public MsgToDB(Address from, Address to, MessageContext context) {
		super(from, to, context);
	}

	@Override
	public void exec(Addressee addressee) throws MessageException {
		if (addressee instanceof DBService) {
			exec((DBService) addressee);
		}
	}

	public abstract void exec(DBService dbService) throws MessageException;
}
