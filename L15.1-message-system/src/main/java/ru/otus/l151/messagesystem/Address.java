package ru.otus.l151.messagesystem;

import java.util.concurrent.atomic.AtomicInteger;

public class Address {

	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
	private final String id;

	public Address() {
		id = String.valueOf(ID_GENERATOR.getAndIncrement());
	}

	public Address(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Address)) {
			return false;
		}
		Address other = (Address) obj;
		return id.equals(other.id);
	}

}
