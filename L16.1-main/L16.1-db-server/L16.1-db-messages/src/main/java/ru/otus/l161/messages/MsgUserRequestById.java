package ru.otus.l161.messages;

public class MsgUserRequestById {

	private final Long id;

	public MsgUserRequestById(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
