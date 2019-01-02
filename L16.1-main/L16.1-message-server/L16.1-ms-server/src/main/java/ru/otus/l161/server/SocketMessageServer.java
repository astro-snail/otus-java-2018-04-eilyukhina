package ru.otus.l161.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import ru.otus.l161.channel.MessageWorker;
import ru.otus.l161.channel.SocketMessageWorker;
import ru.otus.l161.message.Address;
import ru.otus.l161.message.Message;
import ru.otus.l161.message.app.MsgShutdown;

public class SocketMessageServer implements SocketMessageServerMBean {

	private static final Logger logger = Logger.getLogger(SocketMessageServer.class.getName());

	private static final int PORT = 5050;

	private ServerSocket serverSocket;
	private final ExecutorService server;
	private final ConcurrentMap<Address, MessageWorker> workers;
	private final Runnable onStartup;
	private final Runnable onShutdown;

	private volatile boolean running = false;

	public SocketMessageServer() {
		this(null, null);
	}

	public SocketMessageServer(Runnable onStartup, Runnable onShutdown) {
		this.server = Executors.newFixedThreadPool(2);
		this.workers = new ConcurrentHashMap<>();
		this.onStartup = onStartup;
		this.onShutdown = onShutdown;
	}

	public void start() {
		try {
			serverSocket = new ServerSocket(PORT);
			setRunning(true);
			logger.info("Server started on port: " + serverSocket.getLocalPort());

			server.execute(this::routeMessages);
			server.execute(this::acceptClients);

			if (onStartup != null) {
				onStartup.run();
			}

		} catch (IOException e) {
			logger.severe(e.getMessage());
			shutdown();
		}
	}

	private void acceptClients() {
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				Address address = new Address(socket.getInetAddress(), socket.getPort());

				SocketMessageWorker worker = new SocketMessageWorker(socket);
				worker.init();
				worker.addShutdownRegistration(() -> workers.remove(address));
				workers.put(address, worker);
				logger.info("Client connected on port: " + socket.getPort());
			} catch (IOException e) {
				logger.severe(e.getMessage());
			}
		}
	}

	private void routeMessages() {
		while (running) {
			for (MessageWorker worker : workers.values()) {
				Message message = worker.poll();
				if (message != null) {

					logger.info("Message retrieved: " + message);

					try {
						Address messageReceiver = message.getTo();

						if (workers.containsKey(messageReceiver)) {
							workers.get(messageReceiver).put(message);
							logger.info("Message routed to : " + messageReceiver);
						}
					} catch (InterruptedException e) {
						logger.severe(e.getMessage());
					}
				}
			}
		}
	}

	private void broadcastMessage(Message message) {
		for (MessageWorker worker : workers.values()) {
			try {
				worker.put(message);
				logger.info("Shutdown message sent");
			} catch (InterruptedException e) {
				logger.severe(e.getMessage());
			}
		}
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void setRunning(boolean running) {
		this.running = running;

		if (!running) {
			shutdown();
		}
	}

	private void shutdown() {
		broadcastMessage(new Message(null, null, new MsgShutdown()));
		server.shutdown();

		if (onShutdown != null) {
			onShutdown.run();
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
}
