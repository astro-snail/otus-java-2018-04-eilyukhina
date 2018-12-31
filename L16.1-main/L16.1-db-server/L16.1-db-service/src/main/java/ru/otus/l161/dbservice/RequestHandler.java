package ru.otus.l161.dbservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ru.otus.l161.DBHelper;
import ru.otus.l161.cache.CacheConfiguration;
import ru.otus.l161.cache.CacheFactory;
import ru.otus.l161.channel.SocketMessageWorker;
import ru.otus.l161.dataset.UserDataSet;
import ru.otus.l161.message.Address;
import ru.otus.l161.message.Message;
import ru.otus.l161.message.app.MsgShutdown;
import ru.otus.l161.messages.MsgAllUsersRequest;
import ru.otus.l161.messages.MsgAllUsersResponse;
import ru.otus.l161.messages.MsgCacheParametersRequest;
import ru.otus.l161.messages.MsgCacheParametersResponse;
import ru.otus.l161.messages.MsgDeleteUserRequest;
import ru.otus.l161.messages.MsgDeleteUserResponse;
import ru.otus.l161.messages.MsgSaveUserRequest;
import ru.otus.l161.messages.MsgSaveUserResponse;
import ru.otus.l161.messages.MsgUserRequestById;
import ru.otus.l161.messages.MsgUserRequestByName;
import ru.otus.l161.messages.MsgUserResponse;

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

	private Message handleRequest(Message message) throws SQLException {

		Message response = null;

		Object payload = message.getPayload();

		if (message.getMessageType() == MsgShutdown.class) {
			shutdown();
		}

		if (message.getMessageType() == MsgAllUsersRequest.class) {
			List<UserDataSet> users = dbService.loadAll();
			response = new Message(message.getTo(), message.getFrom(), new MsgAllUsersResponse(users));
		}

		if (message.getMessageType() == MsgUserRequestById.class) {
			MsgUserRequestById request = (MsgUserRequestById) payload;
			UserDataSet user = dbService.load(request.getId());
			response = new Message(message.getTo(), message.getFrom(), new MsgUserResponse(user));
		}

		if (message.getMessageType() == MsgUserRequestByName.class) {
			MsgUserRequestByName request = (MsgUserRequestByName) payload;
			UserDataSet user = dbService.loadByName(request.getName());
			response = new Message(message.getTo(), message.getFrom(), new MsgUserResponse(user));
		}

		if (message.getMessageType() == MsgSaveUserRequest.class) {
			MsgSaveUserRequest request = (MsgSaveUserRequest) payload;
			UserDataSet user = dbService.save(request.getUser());
			response = new Message(message.getTo(), message.getFrom(), new MsgSaveUserResponse(user));
		}

		if (message.getMessageType() == MsgDeleteUserRequest.class) {
			MsgDeleteUserRequest request = (MsgDeleteUserRequest) payload;
			String result = dbService.delete(request.getId());
			response = new Message(message.getTo(), message.getFrom(), new MsgDeleteUserResponse(result));
		}

		if (message.getMessageType() == MsgCacheParametersRequest.class) {
			Map<String, String> cacheParams = dbService.getCacheParameters();
			response = new Message(message.getTo(), message.getFrom(), new MsgCacheParametersResponse(cacheParams));
		}

		return response;
	}
}
