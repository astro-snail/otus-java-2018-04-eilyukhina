package ru.otus.l091;

import java.sql.Connection;
import java.sql.SQLException;

import ru.otus.l091.connection.ConnectionHelper;
import ru.otus.l091.dataset.UserDataSet;
import ru.otus.l091.executor.QueryExecutor;

public class Main {

	public static void main(String[] args) throws SQLException {
		try (Connection connection = ConnectionHelper.getConnection()) {
			QueryExecutor executor = new QueryExecutor(connection);

			UserDataSet user = new UserDataSet("John", 35);
			executor.save(user);
			System.out.println("User created: " + user);

			UserDataSet newUser = executor.load(user.getId(), UserDataSet.class);
			System.out.println("User loaded: " + newUser);

			System.out.println("Objects are equal: " + user.equals(newUser));

			user.setAge(40);
			user.setName("John Doe");
			executor.save(user);
			System.out.println("User updated: " + user);

			newUser = executor.load(user.getId(), UserDataSet.class);
			System.out.println("User loaded: " + newUser);

			System.out.println("Objects are equal: " + user.equals(newUser));
		}
	}

}
