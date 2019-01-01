package ru.otus.l161.dbmessages;

import java.sql.SQLException;

import ru.otus.l161.dbservice.DBService;

public abstract class Request {

	public abstract Response execute(DBService dbService) throws SQLException;

	public abstract Class<? extends Response> getResponseType();

}
