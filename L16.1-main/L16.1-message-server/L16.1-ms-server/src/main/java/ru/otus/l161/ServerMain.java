package ru.otus.l161;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import ru.otus.l161.runner.ProcessRunnerImpl;
import ru.otus.l161.server.SocketMessageServer;

public class ServerMain {

	private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

	public static void main(String[] args) throws Exception {
		new ServerMain().start();
	}

	private void start() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName("ru.otus:type=Server");
		SocketMessageServer server = new SocketMessageServer(this::startClients, this::shutdownClients);
		mbs.registerMBean(server, name);

		server.start();
	}

	private void startClients() {
		List<String[]> clientStartCommand = new ArrayList<>();
		clientStartCommand
				.add(new String[] { "java", "-jar", "../../L16.1-db-server/L16.1-db-service/target/db-service.jar" });
		clientStartCommand.add(new String[] { System.getenv("CATALINA_HOME") + "/bin/startup.bat" });

		for (String[] command : clientStartCommand) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						new ProcessRunnerImpl().start(command);
						logger.info("Client started");
					} catch (IOException e) {
						logger.severe(e.getMessage());
					}
				}
			}).start();
		}
	}

	private void shutdownClients() {
		List<String[]> clientStopCommand = new ArrayList<>();
		clientStopCommand.add(new String[] { System.getenv("CATALINA_HOME") + "/bin/shutdown.bat" });

		for (String[] command : clientStopCommand) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						new ProcessRunnerImpl().start(command);
						logger.info("Client stopped");
					} catch (IOException e) {
						logger.severe(e.getMessage());
					}
				}
			}).start();
		}
	}
}
