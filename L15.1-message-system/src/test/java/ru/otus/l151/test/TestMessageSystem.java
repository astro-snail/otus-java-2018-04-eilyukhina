package ru.otus.l151.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.l151.DBHelper;
import ru.otus.l151.app.MessageSystemContext;
import ru.otus.l151.cache.CacheConfiguration;
import ru.otus.l151.cache.CacheFactory;
import ru.otus.l151.dataset.AddressDataSet;
import ru.otus.l151.dataset.PhoneDataSet;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.dbservice.DBServiceHibernateImpl;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.messagesystem.MessageEvent;
import ru.otus.l151.messagesystem.MessageEventListener;
import ru.otus.l151.messagesystem.MessageSystem;
import ru.otus.l151.uiservice.Operation;
import ru.otus.l151.uiservice.UIService;
import ru.otus.l151.uiservice.UIServiceImpl;

class TestMessageSystem {
	
	MessageSystem messageSystem;
	MessageSystemContext context;
	DBService dbService;
	UIService uiService;
	Object result;
	
	@BeforeEach
	void setUp() throws Exception {
		messageSystem = new MessageSystem();
		context = new MessageSystemContext(messageSystem);
		
		dbService = new DBServiceHibernateImpl(context, CacheFactory.getCache(new CacheConfiguration("cacheTime0s.properties")));
		uiService = new UIServiceImpl(context);
		
		dbService.init();
		uiService.init();
		
		messageSystem.start();
		
		// Create 2 users
		DBHelper.doWork(dbService);
	}

	@AfterEach
	void tearDown() throws Exception {
		messageSystem.dispose();
		dbService.shutdown();
		uiService.shutdown();

		messageSystem = null;
		context = null;
		
		dbService = null;
		uiService = null;
		
		result = null;
	}

	@Test
	void testUserByIdMessage() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
	
		MessageContext context = new MessageContext();
		MessageEventListener listener = new MessageEventListener() {
			@Override
			public void messageReceived(MessageEvent event) {
				result = event.getValue();
				latch.countDown();
			}
		};
		context.addListener(listener);
		uiService.handleUserRequest(context, 1L);
		
		latch.await();
				
		assertEquals(dbService.load(1L), result);
	}

	@Test
	void testUserByNameMessage() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
	
		MessageContext context = new MessageContext();
		MessageEventListener listener = new MessageEventListener() {
			@Override
			public void messageReceived(MessageEvent event) {
				result = event.getValue();
				latch.countDown();
			}
		};
		context.addListener(listener);
		uiService.handleUserRequest(context, "Test User 1");
		
		latch.await();
				
		assertEquals(dbService.loadByName("Test User 1"), result);
	}
	
	@Test
	void testAllUsersMessage() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
	
		MessageContext context = new MessageContext();
		MessageEventListener listener = new MessageEventListener() {
			@Override
			public void messageReceived(MessageEvent event) {
				result = event.getValue();
				latch.countDown();
			}
		};
		context.addListener(listener);
		uiService.handleUserRequest(context);
		
		latch.await();
				
		assertEquals(dbService.loadAll(), result);
	}
	
	@Test
	void testAddUserMessage() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
	
		MessageContext context = new MessageContext();
		MessageEventListener listener = new MessageEventListener() {
			@Override
			public void messageReceived(MessageEvent event) {
				result = event.getValue();
				latch.countDown();
			}
		};
		context.addListener(listener);
		
		UserDataSet user = new UserDataSet("New User", 18);
		user.setAddress(new AddressDataSet("New Street"));
		user.addPhone(new PhoneDataSet("911"));
		
		uiService.handleUserRequest(context, Operation.SAVE, user);
		
		latch.await();
		
		UserDataSet newUser = (UserDataSet) result;
		
		assertEquals(user.getName(), newUser.getName());
		assertEquals(user.getAge(), newUser.getAge());
		assertEquals(user.getAddress().getStreet(), newUser.getAddress().getStreet());
		assertEquals(user.getPhones().get(0).getNumber(), newUser.getPhones().get(0).getNumber());
	}
	
	@Test
	void testDeleteUserMessage() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
	
		MessageContext context = new MessageContext();
		MessageEventListener listener = new MessageEventListener() {
			@Override
			public void messageReceived(MessageEvent event) {
				result = event.getValue();
				latch.countDown();
			}
		};
		context.addListener(listener);
		
		UserDataSet user = dbService.load(2L);
		
		uiService.handleUserRequest(context, Operation.DELETE, user);
		
		latch.await();
				
		assertEquals("User deleted", result);
		assertEquals(1, dbService.loadAll().size());
	}
}
