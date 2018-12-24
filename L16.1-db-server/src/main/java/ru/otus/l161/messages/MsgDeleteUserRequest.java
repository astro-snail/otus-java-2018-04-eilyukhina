package ru.otus.l161.messages;

public class MsgDeleteUserRequest {
	
	private final Long id;
	
	public MsgDeleteUserRequest(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
