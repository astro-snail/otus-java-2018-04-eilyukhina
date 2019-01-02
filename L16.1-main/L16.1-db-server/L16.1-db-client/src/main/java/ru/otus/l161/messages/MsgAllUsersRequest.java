package ru.otus.l161.messages;

import ru.otus.l161.handler.RequestHandler;

public class MsgAllUsersRequest extends Request {

	public MsgAllUsersRequest() {

	}

	@Override
	public Response execute(RequestHandler handler) {
		return new MsgAllUsersResponse(handler.getAllUsers());
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgAllUsersResponse.class;
	}
}
