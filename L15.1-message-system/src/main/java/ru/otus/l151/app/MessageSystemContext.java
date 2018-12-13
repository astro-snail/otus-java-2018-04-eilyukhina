package ru.otus.l151.app;

import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.Addressee;
import ru.otus.l151.messagesystem.MessageSystem;

public class MessageSystemContext {
	
	private final MessageSystem messageSystem;
	private Address dbServiceAddress;
	private Address uiServiceAddress;
	
	public MessageSystemContext(MessageSystem messageSystem) {
		this.messageSystem = messageSystem;
	}
	
	public MessageSystem getMessageSystem() {
		return messageSystem;
	}
	
	public void registerDBService(Addressee addressee) {
		dbServiceAddress = messageSystem.registerAddressee(addressee);
	}
	
	public void registerUIService(Addressee addressee) {
		uiServiceAddress = messageSystem.registerAddressee(addressee);
	}
	
	public Address getDBServiceAddress() {
		return dbServiceAddress;
	}
	
	public Address getUIServiceAddress() {
		return uiServiceAddress;
	}
}
