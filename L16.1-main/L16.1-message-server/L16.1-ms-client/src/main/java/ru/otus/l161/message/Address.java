package ru.otus.l161.message;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

import com.google.gson.Gson;

public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	private final InetAddress host;
	private final int port;

	public Address(int port) throws IOException {
		this(InetAddress.getLocalHost(), port);
	}

	public Address(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}

	public InetAddress getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	@Override
	public int hashCode() {
		return host.hashCode() + port;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Address)) {
			return false;
		}
		Address other = (Address) obj;
		return host.equals(other.host) && port == other.port;
	}

}
