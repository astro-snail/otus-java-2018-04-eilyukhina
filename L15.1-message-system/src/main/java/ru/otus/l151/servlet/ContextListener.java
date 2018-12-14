package ru.otus.l151.servlet;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l151.DBHelper;
import ru.otus.l151.dbservice.DBService;
import ru.otus.l151.uiservice.UIService;
import ru.otus.l151.messagesystem.MessageSystem;

public class ContextListener implements ServletContextListener {
	
	@Autowired
	private MessageSystem messageSystem;
	
	@Autowired
	private UIService uiService;
	
	@Autowired
	private DBService dbService;

	@Override
	public void contextInitialized(ServletContextEvent event) {
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, event.getServletContext());

	    // Create some initial data - would not be required in real life
	    DBHelper.doWork(dbService);
       	
       	dbService.register();
       	uiService.register();
       	
       	messageSystem.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			messageSystem.dispose();
			dbService.shutdown();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
