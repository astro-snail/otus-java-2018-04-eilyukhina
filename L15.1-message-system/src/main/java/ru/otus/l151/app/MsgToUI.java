package ru.otus.l151.app;

import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.Addressee;
import ru.otus.l151.messagesystem.Message;
import ru.otus.l151.messagesystem.MessageException;

public abstract class MsgToUI extends Message {
	
	public MsgToUI(Address from, Address to) {
		super(from, to);
	}
	
	@Override
	public void exec(Addressee addressee) throws MessageException {
		if (addressee instanceof UIService) {
			exec((UIService) addressee);
		}
	}
	
	public abstract void exec(UIService uiService) throws MessageException;

}
