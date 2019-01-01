package ru.otus.l161.dbmessages;

import java.sql.SQLException;

import ru.otus.l161.dbservice.DBService;

public class MsgCacheParametersRequest extends Request {

	public MsgCacheParametersRequest() {

	}

	@Override
	public Response execute(DBService dbService) throws SQLException {
		return new MsgCacheParametersResponse(dbService.getCacheParameters());
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgCacheParametersResponse.class;
	}
}
