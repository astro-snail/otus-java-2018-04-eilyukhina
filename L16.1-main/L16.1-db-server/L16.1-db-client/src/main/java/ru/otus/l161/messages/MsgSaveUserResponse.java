package ru.otus.l161.messages;

import ru.otus.l161.model.User;

public class MsgSaveUserResponse extends Response {

	private final User user;

	public MsgSaveUserResponse(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public Object getValue() {
		return getUser();
	}
}
