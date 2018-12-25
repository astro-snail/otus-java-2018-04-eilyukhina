package ru.otus.l151;

import java.sql.SQLException;

import ru.otus.l151.dataset.*;
import ru.otus.l151.dbservice.DBService;

public class DBHelper {

	public static void doWork(DBService dbService) {
		try {
			UserDataSet user = new UserDataSet("Test User 1", 25);
			user.setAddress(new AddressDataSet("Green Lane"));
			user.addPhone(new PhoneDataSet("1234567890"));
			dbService.save(user);

			user = new UserDataSet("Test User 2", 35);
			user.setAddress(new AddressDataSet("New Street"));
			user.addPhone(new PhoneDataSet("0147258369"));
			dbService.save(user);

		} catch (SQLException e) {

		}
	}

}
