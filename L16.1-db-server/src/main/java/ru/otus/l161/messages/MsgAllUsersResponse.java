package ru.otus.l161.messages;

import java.util.List;

import com.google.gson.Gson;

import ru.otus.l161.messages.dataset.UserDataSet;

public class MsgAllUsersResponse {
	
	private final List<UserDataSet> users;
	
	public MsgAllUsersResponse(List<UserDataSet> users) {
		this.users = users;
	}
	
	public List<UserDataSet> getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
