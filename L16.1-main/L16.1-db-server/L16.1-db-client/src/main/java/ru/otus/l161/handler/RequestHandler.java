package ru.otus.l161.handler;

import java.util.List;
import java.util.Map;

import ru.otus.l161.model.User;

public interface RequestHandler {

	void init();

	void shutdown();

	List<User> getAllUsers();

	User getUserById(Long id);

	User getUserByName(String name);

	User saveUser(User user);

	String deleteUser(Long id);

	String deleteUser(User user);

	Map<String, String> getCacheParameters();
}
