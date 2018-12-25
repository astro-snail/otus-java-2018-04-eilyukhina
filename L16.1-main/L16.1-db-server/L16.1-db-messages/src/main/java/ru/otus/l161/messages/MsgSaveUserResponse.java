package ru.otus.l161.messages;

import ru.otus.l161.dataset.UserDataSet;

public class MsgSaveUserResponse {

	private final UserDataSet user;

	public MsgSaveUserResponse(UserDataSet user) {
		this.user = user;
	}

	public UserDataSet getUser() {
		return user;
	}
}
