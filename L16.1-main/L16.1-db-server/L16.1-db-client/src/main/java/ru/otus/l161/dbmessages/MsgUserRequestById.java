package ru.otus.l161.dbmessages;

import java.sql.SQLException;

import ru.otus.l161.dbservice.DBService;

public class MsgUserRequestById extends Request {

	private final Long id;

	public MsgUserRequestById(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public Response execute(DBService dbService) throws SQLException {
		return new MsgUserResponse(dbService.load(id));
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgUserResponse.class;
	}
}
