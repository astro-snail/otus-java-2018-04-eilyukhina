package ru.otus.l161.messages;

public class MsgDeleteUserResponse extends Response {

	private final String message;

	public MsgDeleteUserResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public Object getValue() {
		return getMessage();
	}
}
