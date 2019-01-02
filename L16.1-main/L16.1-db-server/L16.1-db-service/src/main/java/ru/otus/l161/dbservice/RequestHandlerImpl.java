package ru.otus.l161.dbservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ru.otus.l161.DBHelper;
import ru.otus.l161.cache.CacheConfiguration;
import ru.otus.l161.cache.CacheFactory;
import ru.otus.l161.channel.SocketMessageWorker;
import ru.otus.l161.dataset.AddressDataSet;
import ru.otus.l161.dataset.PhoneDataSet;
import ru.otus.l161.dataset.UserDataSet;
import ru.otus.l161.handler.RequestHandler;
import ru.otus.l161.message.Address;
import ru.otus.l161.message.Message;
import ru.otus.l161.message.app.MsgShutdown;
import ru.otus.l161.messages.Request;
import ru.otus.l161.messages.Response;
import ru.otus.l161.model.Phone;
import ru.otus.l161.model.User;

public class RequestHandlerImpl implements RequestHandler {

	private static final Logger logger = Logger.getLogger(RequestHandlerImpl.class.getName());

	private static final int SERVER_PORT = 5050;
	private static final int LOCAL_PORT = 50200;

	private final DBService dbService;

	private SocketMessageWorker worker;
	private Thread messageHandler;

	public RequestHandlerImpl() {
		dbService = new DBServiceHibernateImpl(CacheFactory.getCache(new CacheConfiguration()));
	}

	@Override
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

	@Override
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
				Response response = ((Request) payload).execute(this);
				responseMessage = new Message(requestMessage.getTo(), requestMessage.getFrom(), response);
			}
		}

		return responseMessage;
	}

	private ru.otus.l161.model.Address copyFromDataSet(AddressDataSet address) {
		return new ru.otus.l161.model.Address(address.getId(), address.getStreet());
	}

	private AddressDataSet copyToDataSet(ru.otus.l161.model.Address address) {
		return new AddressDataSet(address.getId(), address.getStreet());
	}

	private List<Phone> copyFromDataSet(List<PhoneDataSet> phones) {
		List<Phone> copy = new ArrayList<>();
		for (PhoneDataSet phone : phones) {
			copy.add(copyFromDataSet(phone));
		}
		return copy;
	}

	private List<PhoneDataSet> copyToDataSet(List<Phone> phones) {
		List<PhoneDataSet> copy = new ArrayList<>();
		for (Phone phone : phones) {
			copy.add(copyToDataSet(phone));
		}
		return copy;
	}

	private Phone copyFromDataSet(PhoneDataSet phone) {
		return new Phone(phone.getId(), phone.getNumber());
	}

	private PhoneDataSet copyToDataSet(Phone phone) {
		return new PhoneDataSet(phone.getId(), phone.getNumber());
	}

	private User copyFromDataSet(UserDataSet user) {
		User copy = new User(user.getId(), user.getName(), user.getAge());
		copy.setAddress(copyFromDataSet(user.getAddress()));
		copy.setPhones(copyFromDataSet(user.getPhones()));
		return copy;
	}

	private UserDataSet copyToDataSet(User user) {
		UserDataSet copy = new UserDataSet(user.getId(), user.getName(), user.getAge());
		copy.setAddress(copyToDataSet(user.getAddress()));
		copy.setPhones(copyToDataSet(user.getPhones()));
		return copy;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		try {
			for (UserDataSet user : dbService.loadAll()) {
				users.add(copyFromDataSet(user));
			}
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
		return users;
	}

	@Override
	public User getUserById(Long id) {
		User user = null;
		try {
			user = copyFromDataSet(dbService.load(id));
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
		return user;
	}

	@Override
	public User getUserByName(String name) {
		User user = null;
		try {
			user = copyFromDataSet(dbService.loadByName(name));
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
		return user;
	}

	@Override
	public User saveUser(User user) {
		User savedUser = null;
		try {
			savedUser = copyFromDataSet(dbService.save(copyToDataSet(user)));
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
		return savedUser;
	}

	@Override
	public String deleteUser(Long id) {
		String result = null;
		try {
			result = dbService.delete(id);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
		return result;
	}

	@Override
	public String deleteUser(User user) {
		String result = null;
		try {
			result = dbService.delete(copyToDataSet(user));
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}
		return result;
	}

	@Override
	public Map<String, String> getCacheParameters() {
		return dbService.getCacheParameters();
	}
}
