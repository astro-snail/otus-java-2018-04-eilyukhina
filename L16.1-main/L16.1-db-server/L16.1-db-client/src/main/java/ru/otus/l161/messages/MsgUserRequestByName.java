package ru.otus.l161.messages;

import ru.otus.l161.handler.RequestHandler;

public class MsgUserRequestByName extends Request {

	private final String name;

	public MsgUserRequestByName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Response execute(RequestHandler handler) {
		return new MsgUserResponse(handler.getUserByName(name));
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgUserResponse.class;
	}
}
