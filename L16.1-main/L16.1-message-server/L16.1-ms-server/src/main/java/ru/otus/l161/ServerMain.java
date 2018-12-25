package ru.otus.l161;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import ru.otus.l161.server.SocketMessageServer;

public class ServerMain {

	public static void main(String[] args) throws Exception {
		new ServerMain().start();
	}

	private void start() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName("ru.otus:type=Server");
		SocketMessageServer server = new SocketMessageServer();
		mbs.registerMBean(server, name);

		server.start();
	}
}
