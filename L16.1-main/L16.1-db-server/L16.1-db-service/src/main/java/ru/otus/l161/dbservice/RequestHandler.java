package ru.otus.l161.dbservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import ru.otus.l161.DBHelper;
import ru.otus.l161.cache.CacheConfiguration;
import ru.otus.l161.cache.CacheFactory;
import ru.otus.l161.channel.SocketMessageWorker;
import ru.otus.l161.dbmessages.Request;
import ru.otus.l161.dbmessages.Response;
import ru.otus.l161.message.Address;
import ru.otus.l161.message.Message;
import ru.otus.l161.message.app.MsgShutdown;

public class RequestHandler {

	private static final Logger logger = Logger.getLogger(RequestHandler.class.getName());

	private static final int SERVER_PORT = 5050;
	private static final int LOCAL_PORT = 50200;

	private final DBService dbService;

	private SocketMessageWorker worker;
	private Thread messageHandler;

	public RequestHandler() {
		dbService = new DBServiceHibernateImpl(CacheFactory.getCache(new CacheConfiguration()));
	}

	public void init() {

		dbService.init();
		DBHelper.doWork(dbService);

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

							Message response = handleRequest(message);

							if (response != null) {
								worker.put(response);
								logger.info("Message sent: " + response);
							}
						}
					} catch (SQLException | InterruptedException e) {
						logger.severe(e.getMessage());
					}
				}
			});

			messageHandler.start();

		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

	public void shutdown() {
		messageHandler.interrupt();
		worker.close();

		dbService.shutdown();
	}

	private Message handleRequest(Message requestMessage) throws SQLException {

		Message responseMessage = null;

		if (requestMessage.getMessageType() == MsgShutdown.class) {

			shutdown();

		} else {

			Object payload = requestMessage.getPayload();

			if (payload instanceof Request) {
				Response response = ((Request) payload).execute(dbService);
				responseMessage = new Message(requestMessage.getTo(), requestMessage.getFrom(), response);
			}
		}

		return responseMessage;
	}
}
