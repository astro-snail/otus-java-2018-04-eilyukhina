package ru.otus.l161.messages;

import java.util.List;

import ru.otus.l161.model.User;

public class MsgAllUsersResponse extends Response {

	private final List<User> users;

	public MsgAllUsersResponse(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	@Override
	public Object getValue() {
		return getUsers();
	}
}
