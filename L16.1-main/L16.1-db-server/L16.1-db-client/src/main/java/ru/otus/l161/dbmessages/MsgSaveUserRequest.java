package ru.otus.l161.dbmessages;

import java.sql.SQLException;

import ru.otus.l161.dataset.UserDataSet;
import ru.otus.l161.dbservice.DBService;

public class MsgSaveUserRequest extends Request {

	private final UserDataSet user;

	public MsgSaveUserRequest(UserDataSet user) {
		this.user = user;
	}

	public UserDataSet getUser() {
		return user;
	}

	@Override
	public Response execute(DBService dbService) throws SQLException {
		return new MsgSaveUserResponse(dbService.save(user));
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgSaveUserResponse.class;
	}
}
