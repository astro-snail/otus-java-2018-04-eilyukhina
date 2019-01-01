package ru.otus.l161.dbmessages;

import ru.otus.l161.dataset.UserDataSet;

public class MsgUserResponse extends Response {

	private final UserDataSet user;

	public MsgUserResponse(UserDataSet user) {
		this.user = user;
	}

	public UserDataSet getUser() {
		return user;
	}

	@Override
	public Object getValue() {
		return getUser();
	}
}
