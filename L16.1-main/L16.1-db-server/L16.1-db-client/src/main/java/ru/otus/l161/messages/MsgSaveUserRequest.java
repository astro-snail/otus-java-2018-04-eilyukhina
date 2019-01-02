package ru.otus.l161.messages;

import ru.otus.l161.handler.RequestHandler;
import ru.otus.l161.model.User;

public class MsgSaveUserRequest extends Request {

	private final User user;

	public MsgSaveUserRequest(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public Response execute(RequestHandler handler) {
		return new MsgSaveUserResponse(handler.saveUser(user));
	}

	@Override
	public Class<? extends Response> getResponseType() {
		return MsgSaveUserResponse.class;
	}
}
