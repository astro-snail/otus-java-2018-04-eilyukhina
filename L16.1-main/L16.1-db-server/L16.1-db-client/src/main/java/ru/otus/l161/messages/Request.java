package ru.otus.l161.messages;

import ru.otus.l161.handler.RequestHandler;

public abstract class Request {

	public abstract Response execute(RequestHandler handler);

	public abstract Class<? extends Response> getResponseType();

}
