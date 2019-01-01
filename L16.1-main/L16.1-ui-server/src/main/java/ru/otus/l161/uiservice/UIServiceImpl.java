package ru.otus.l161.uiservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import ru.otus.l161.channel.SocketMessageWorker;
import ru.otus.l161.dbmessages.Request;
import ru.otus.l161.dbmessages.Response;
import ru.otus.l161.message.Address;
import ru.otus.l161.message.Message;
import ru.otus.l161.message.MessageEvent;
import ru.otus.l161.message.MessageEventListener;
import ru.otus.l161.message.app.MsgShutdown;

public class UIServiceImpl implements UIService {

	private static final Logger logger = Logger.getLogger(UIServiceImpl.class.getName());

	private static final int SERVER_PORT = 5050;
	private static final int LOCAL_PORT = 50100;
	private static final int DB_PORT = 50200;

	private SocketMessageWorker worker;
	private Thread messageHandler;
	private ConcurrentMap<String, List<MessageEventListener>> listeners = new ConcurrentHashMap<>();

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
	public void doRequest(Request request, MessageEventListener listener) {
		try {
			Address from = new Address(LOCAL_PORT);
			Address to = new Address(DB_PORT);

			Message message = new Message(from, to, request);
			worker.put(message);
			logger.info("Message sent: " + message);

			if (!(listener == null || request.getResponseType() == null)) {
				addListener(request.getResponseType().getName(), listener);
			}

		} catch (IOException | InterruptedException e) {
			logger.severe(e.getMessage());
		}
	}

	@Override
	public void handleResponse(Message message) {

		if (message.getMessageType() == MsgShutdown.class) {

			shutdown();

		} else {

			Object payload = message.getPayload();
			Object value = null;

			if (payload instanceof Response) {
				value = ((Response) payload).getValue();
			}

			for (MessageEventListener listener : removeListeners(message.getMessageType().getName())) {
				listener.messageReceived(new MessageEvent(message, value));
			}
		}
	}

	@Override
	public void shutdown() {
		messageHandler.interrupt();
		worker.close();
		listeners.clear();
	}

	private void addListener(String event, MessageEventListener listener) {

		if (!listeners.containsKey(event)) {
			listeners.put(event, new ArrayList<>());
		}

		listeners.get(event).add(listener);
	}

	private List<MessageEventListener> removeListeners(String event) {
		return listeners.remove(event);
	}
}
