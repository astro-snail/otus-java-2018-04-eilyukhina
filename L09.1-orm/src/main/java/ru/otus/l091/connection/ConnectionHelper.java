package ru.otus.l091.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHelper {

	static private Connection connection;

	public static Connection getConnection() throws SQLException {
		if (connection == null) {
			String url = "jdbc:postgresql://localhost:5432/orm";
			Properties props = new Properties();
			props.setProperty("user","postgres");
			props.setProperty("password","admin");
			connection = DriverManager.getConnection(url, props);
		}
		return connection;
	}

	public static void closeConnection() throws SQLException {
		if (!(connection == null || connection.isClosed())) {
			connection.close();
		}
	}

}
