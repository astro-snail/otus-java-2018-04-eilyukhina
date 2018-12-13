package ru.otus.l151.servlet;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l151.app.DBService;
import ru.otus.l151.app.UIService;
import ru.otus.l151.dataset.AddressDataSet;
import ru.otus.l151.dataset.PhoneDataSet;
import ru.otus.l151.dataset.UserDataSet;
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

	    UserDataSet user = null;
	    
       	try {
       		user = new UserDataSet("Test User 1", 25);
       		user.setAddress(new AddressDataSet("Green Lane"));
       		user.addPhone(new PhoneDataSet("1234567890"));
			dbService.save(user);
		
       	  	user = new UserDataSet("Test User 2", 35);
       	  	user.setAddress(new AddressDataSet("New Street"));
       	  	user.addPhone(new PhoneDataSet("0147258369"));
       	  	dbService.save(user);
       	 
       	} catch (SQLException e) {

		}
       	
       	messageSystem.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			messageSystem.dispose();
			dbService.shutdown();
		} catch (SQLException e) {
			
		}
	}

}
