package ru.otus.l161.messages;

import ru.otus.l161.model.User;

public class MsgUserResponse extends Response {

	private final User user;

	public MsgUserResponse(User user) {
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
