package ru.otus.l161.dbmessages;

import ru.otus.l161.dataset.UserDataSet;

public class MsgSaveUserResponse extends Response {

	private final UserDataSet user;

	public MsgSaveUserResponse(UserDataSet user) {
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
