package ru.otus.l161.dbmessages;

import java.sql.SQLException;

import ru.otus.l161.dbservice.DBService;

public class MsgAllUsersRequest extends Request {

	public MsgAllUsersRequest() {

	}

	@Override
	public Response execute(DBService dbService) throws SQLException {
		return new MsgAllUsersResponse(dbService.loadAll());
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgAllUsersResponse.class;
	}
}
