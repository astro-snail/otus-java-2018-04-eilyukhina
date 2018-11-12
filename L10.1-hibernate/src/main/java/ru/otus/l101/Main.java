package ru.otus.l101;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.otus.l101.dataset.*;
import ru.otus.l101.dbservice.*;

public class Main {

	public static void main(String[] args) throws SQLException {
		System.out.println("Hibernate");
		testService(new DBServiceHibernateImpl());
		
		System.out.println("myORM");
		testService(new DBServiceImpl()); 
	}

	private static void testService(DBService service) throws SQLException {
		
		List<PhoneDataSet> phones = new ArrayList<>();
		phones.add(new PhoneDataSet("0147258369"));
		phones.add(new PhoneDataSet("0123456789"));
		
		UserDataSet user = new UserDataSet("Johnny Johnny", 35, new AddressDataSet("My Green Street"), phones); 
		service.save(user);
		System.out.println("User created: " + user);
		
		phones = new ArrayList<>();
		phones.add(new PhoneDataSet("0147258369"));
		phones.add(new PhoneDataSet("0123456789"));
		
		user = new UserDataSet("Johnny Boy", 25, new AddressDataSet("My Wide Street"), phones); 
		service.save(user);
		System.out.println("User created: " + user);
		
		List<UserDataSet> users = service.loadAll();
		System.out.println("Users in DB: " + users.size());
		
		for (UserDataSet theUser : service.loadAll()) {
			System.out.println(theUser);
		}
		
		for (UserDataSet theUser : service.loadAll()) {
			service.delete(theUser);
			System.out.println("User deleted: " + theUser);
		}
		
		System.out.println("Users in DB: " + service.loadAll().size());
		
		service.shutdown();
	}
	
}
