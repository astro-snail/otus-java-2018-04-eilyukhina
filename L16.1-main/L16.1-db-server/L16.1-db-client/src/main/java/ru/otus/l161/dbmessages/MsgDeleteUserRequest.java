package ru.otus.l161.dbmessages;

import java.sql.SQLException;

import ru.otus.l161.dbservice.DBService;

public class MsgDeleteUserRequest extends Request {

	private final Long id;

	public MsgDeleteUserRequest(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public Response execute(DBService dbService) throws SQLException {
		return new MsgDeleteUserResponse(dbService.delete(id));
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgDeleteUserResponse.class;
	}
}
