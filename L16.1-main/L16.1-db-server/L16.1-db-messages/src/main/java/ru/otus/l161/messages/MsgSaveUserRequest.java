package ru.otus.l161.messages;

import ru.otus.l161.dataset.UserDataSet;

public class MsgSaveUserRequest {

	private final UserDataSet user;
	
	public MsgSaveUserRequest(UserDataSet user) {
		this.user = user;
	}

	public UserDataSet getUser() {
		return user;
	}
}
