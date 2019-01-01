package ru.otus.l161.dbmessages;

import java.sql.SQLException;

import ru.otus.l161.dbservice.DBService;

public class MsgUserRequestByName extends Request {

	private final String name;

	public MsgUserRequestByName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Response execute(DBService dbService) throws SQLException {
		return new MsgUserResponse(dbService.loadByName(name));
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgUserResponse.class;
	}
}
