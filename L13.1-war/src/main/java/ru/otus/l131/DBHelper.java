package ru.otus.l131;

import java.sql.SQLException;

import ru.otus.l131.dataset.*;
import ru.otus.l131.dbservice.DBService;

public class DBHelper {
	
	// Simulates DB activity
	public static void doWork(DBService dbService) {
		try {
			UserDataSet user = new UserDataSet("Johnny Johnny", 35);
			user.setAddress(new AddressDataSet("My Green Street"));
			user.addPhone(new PhoneDataSet("0147258369"));
			user.addPhone(new PhoneDataSet("0123456789")); 
			dbService.save(user);
					
			user = dbService.load(user.getId());
						
			user = dbService.load(user.getId());
					
			user = dbService.load(user.getId());
					
			dbService.loadAddressByUserId(user.getId());
			
			dbService.loadPhoneById(1L);
			
			user = dbService.load(user.getId());
			
			dbService.delete(user);
						
		} catch (SQLException e) {
					
		}
	}

}
