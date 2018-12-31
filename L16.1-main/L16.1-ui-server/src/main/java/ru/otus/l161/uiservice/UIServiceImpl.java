package ru.otus.l161.uiservice;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import ru.otus.l161.channel.SocketMessageWorker;
import ru.otus.l161.message.Address;
import ru.otus.l161.message.Message;
import ru.otus.l161.message.MessageEvent;
import ru.otus.l161.message.MessageEventListener;
import ru.otus.l161.message.app.MsgShutdown;
import ru.otus.l161.messages.MsgAllUsersResponse;
import ru.otus.l161.messages.MsgCacheParametersResponse;
import ru.otus.l161.messages.MsgDeleteUserResponse;
import ru.otus.l161.messages.MsgSaveUserResponse;
import ru.otus.l161.messages.MsgUserResponse;

public class UIServiceImpl implements UIService {

	private static final Logger logger = Logger.getLogger(UIServiceImpl.class.getName());

	private static final int SERVER_PORT = 5050;
	private static final int LOCAL_PORT = 50100;
	private static final int DB_PORT = 50200;

	private SocketMessageWorker worker;
	private Thread messageHandler;
	private ConcurrentMap<String, MessageEventListener> listeners = new ConcurrentHashMap<>();

	@Override
	public void init() {
		try {
			Address server = new Address(SERVER_PORT);
			Address client = new Address(LOCAL_PORT);

			worker = new SocketMessageWorker(server, client);
			worker.init();

			messageHandler = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						while (!worker.isClosed()) {
							Message message = worker.take();
							logger.info("Message received: " + message);

							handleResponse(message);
						}
					} catch (InterruptedException e) {
						logger.severe(e.getMessage());
					}
				}
			});

			messageHandler.start();

		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	@Override
	public void handleRequest(Object request, Class<?> responseType, MessageEventListener listener) {
		try {
			Address from = new Address(LOCAL_PORT);
			Address to = new Address(DB_PORT);

			Message message = new Message(from, to, request);
			worker.put(message);
			logger.info("Message sent: " + message);

			if (!(responseType == null || listener == null)) {
				addListener(responseType.getName(), listener);
			}
		} catch (IOException | InterruptedException e) {
			logger.severe(e.getMessage());
		}
	}

	@Override
	public void handleResponse(Message message) {

		Object payload = message.getPayload();

		Object response = null;

		if (message.getMessageType() == MsgShutdown.class) {
			shutdown();
		}

		if (message.getMessageType() == MsgCacheParametersResponse.class) {
			response = ((MsgCacheParametersResponse) payload).getCacheParametes();
		}

		if (message.getMessageType() == MsgAllUsersResponse.class) {
			response = ((MsgAllUsersResponse) payload).getUsers();
		}

		if (message.getMessageType() == MsgUserResponse.class) {
			response = ((MsgUserResponse) payload).getUser();
		}

		if (message.getMessageType() == MsgSaveUserResponse.class) {
			response = ((MsgSaveUserResponse) payload).getUser();
		}

		if (message.getMessageType() == MsgDeleteUserResponse.class) {
			response = ((MsgDeleteUserResponse) payload).getMessage();
		}

		MessageEventListener listener = removeListener(message.getMessageType().getName());
		if (listener != null) {
			listener.messageReceived(new MessageEvent(message, response));
		}
	}

	@Override
	public void shutdown() {
		messageHandler.interrupt();
		worker.close();
		listeners.clear();
	}

	private void addListener(String event, MessageEventListener listener) {
		listeners.put(event, listener);
	}

	private MessageEventListener removeListener(String event) {
		return listeners.remove(event);
	}

}
