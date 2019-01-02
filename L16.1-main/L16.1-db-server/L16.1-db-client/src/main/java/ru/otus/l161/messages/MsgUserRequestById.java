package ru.otus.l161.messages;

import ru.otus.l161.handler.RequestHandler;

public class MsgUserRequestById extends Request {

	private final Long id;

	public MsgUserRequestById(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public Response execute(RequestHandler handler) {
		return new MsgUserResponse(handler.getUserById(id));
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgUserResponse.class;
	}
}
