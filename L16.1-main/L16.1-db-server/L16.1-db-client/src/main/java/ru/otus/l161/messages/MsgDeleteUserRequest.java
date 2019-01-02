package ru.otus.l161.messages;

import ru.otus.l161.handler.RequestHandler;

public class MsgDeleteUserRequest extends Request {

	private final Long id;

	public MsgDeleteUserRequest(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public Response execute(RequestHandler handler) {
		return new MsgDeleteUserResponse(handler.deleteUser(id));
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgDeleteUserResponse.class;
	}
}
