package ru.otus.l131.servlet;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l131.dbservice.DBService;

public class ContextListener implements ServletContextListener {

	@Autowired
	private DBService dbService;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, event.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			dbService.shutdown();
		} catch (SQLException e) {

		}
	}

}
