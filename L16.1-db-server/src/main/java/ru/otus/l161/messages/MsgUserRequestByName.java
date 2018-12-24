package ru.otus.l161.messages;

public class MsgUserRequestByName {
	
	private final String name;
	
	public MsgUserRequestByName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
