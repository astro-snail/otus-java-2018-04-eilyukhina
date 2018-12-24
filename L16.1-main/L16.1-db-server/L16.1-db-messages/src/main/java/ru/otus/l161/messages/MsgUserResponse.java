package ru.otus.l161.messages;

import ru.otus.l161.dataset.UserDataSet;

public class MsgUserResponse {
	
	private final UserDataSet user;
	
	public MsgUserResponse(UserDataSet user) {
		this.user = user;
	}
	
	public UserDataSet getUser() {
		return user;
	}

}
