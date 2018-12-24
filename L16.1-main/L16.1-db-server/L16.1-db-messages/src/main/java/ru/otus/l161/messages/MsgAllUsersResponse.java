package ru.otus.l161.messages;

import java.util.List;

import ru.otus.l161.dataset.UserDataSet;

public class MsgAllUsersResponse {
	
	private final List<UserDataSet> users;
	
	public MsgAllUsersResponse(List<UserDataSet> users) {
		this.users = users;
	}
	
	public List<UserDataSet> getUsers() {
		return users;
	}

}
