package ru.otus.l091.test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.l091.connection.ConnectionHelper;
import ru.otus.l091.dataset.UserDataSet;
import ru.otus.l091.executor.QueryExecutor;

class QueryExecutorTest {
	
	private static Connection connection;
	private QueryExecutor executor;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		connection = ConnectionHelper.getConnection();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		ConnectionHelper.closeConnection();
	}

	@BeforeEach
	void setUp() throws Exception {
		executor = new QueryExecutor(connection);
	}

	@AfterEach
	void tearDown() throws Exception {
		executor = null;
	}

	@Test
	void testIdReturned() throws SQLException {
		UserDataSet user = new UserDataSet("Anna", 25); 
		executor.save(user);
		
		assertNotEquals(0, user.getId());
	}
	
	@Test
	void testDifferentIdRetirned() throws SQLException {
		UserDataSet user1 = new UserDataSet("Anna", 25); 
		executor.save(user1);
		
		UserDataSet user2 = new UserDataSet("Anna", 25); 
		executor.save(user2);
		
		assertNotEquals(user1.getId(), user2.getId());
	}


	@Test
	void testObjectUpdated() throws SQLException {
		UserDataSet user = new UserDataSet("Anna", 25); 
		executor.save(user);
		
		user.setAge(20);
		user.setName("Anna-Maria");
		executor.save(user);
		
		UserDataSet newUser = executor.load(user.getId(), UserDataSet.class);
		assertEquals(user, newUser);
	}
	
	@Test
	void testObjectLoaded() throws SQLException {
		UserDataSet user1 = new UserDataSet("Anna", 25); 
		executor.save(user1);
		
		UserDataSet user2 = new UserDataSet("Anna", 25); 
		executor.save(user2);
		
		UserDataSet newUser = executor.load(user1.getId(), UserDataSet.class);
		
		assertEquals(user1, newUser);
		assertNotEquals(user2, newUser);
	}

}
