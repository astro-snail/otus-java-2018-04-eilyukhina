package ru.otus.l161.dbmessages;

import java.util.List;

import ru.otus.l161.dataset.UserDataSet;

public class MsgAllUsersResponse extends Response {

	private final List<UserDataSet> users;

	public MsgAllUsersResponse(List<UserDataSet> users) {
		this.users = users;
	}

	public List<UserDataSet> getUsers() {
		return users;
	}

	@Override
	public Object getValue() {
		return getUsers();
	}
}
