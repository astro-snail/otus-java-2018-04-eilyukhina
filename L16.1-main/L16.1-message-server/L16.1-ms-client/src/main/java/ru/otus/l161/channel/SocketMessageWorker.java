package ru.otus.l161.channel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import ru.otus.l161.message.Address;
import ru.otus.l161.message.Message;

public class SocketMessageWorker implements MessageWorker {

	private static final Logger logger = Logger.getLogger(SocketMessageWorker.class.getName());
	private static final int WORKER_COUNT = 2;

	private final BlockingQueue<Message> input = new LinkedBlockingQueue<Message>();
	private final BlockingQueue<Message> output = new LinkedBlockingQueue<Message>();

	private final ExecutorService executor;
	private final Socket socket;

	private final List<Runnable> shutdownRegistrations;

	public SocketMessageWorker(Address server, Address client) throws IOException {
		this(new Socket(server.getHost(), server.getPort(), client.getHost(), client.getPort()));
	}

	public SocketMessageWorker(Socket socket) {
		this.socket = socket;
		this.executor = Executors.newFixedThreadPool(WORKER_COUNT);
		this.shutdownRegistrations = new ArrayList<>();
	}

	public void init() {
		executor.execute(this::receiveMessage);
		executor.execute(this::sendMessage);
	}

	@Override
	public void put(Message message) throws InterruptedException {
		output.put(message);
	}

	@Override
	public Message poll() {
		return input.poll();
	}

	@Override
	public Message take() throws InterruptedException {
		return input.take();
	}

	@Override
	public void close() {
		shutdownRegistrations.forEach(Runnable::run);
		shutdownRegistrations.clear();

		executor.shutdownNow();
		try {
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addShutdownRegistration(Runnable runnable) {
		shutdownRegistrations.add(runnable);
	}

	public boolean isClosed() {
		return socket.isClosed();
	}

	private void sendMessage() {
		try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
			while (socket.isConnected() && !socket.isClosed()) {
				Message message = output.take();
				out.writeObject(message);
				out.flush();
			}
		} catch (InterruptedException | IOException e) {
			logger.severe(e.getMessage());
		} finally {
			close();
		}
	}

	private void receiveMessage() {
		try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
			while (socket.isConnected() && !socket.isClosed()) {
				Message message = (Message) in.readObject();
				input.put(message);
			}
		} catch (ClassNotFoundException | InterruptedException | IOException e) {
			logger.severe(e.getMessage());
		} finally {
			close();
		}
	}
}
