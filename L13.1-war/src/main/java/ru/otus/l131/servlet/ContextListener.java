package ru.otus.l131.servlet;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ru.otus.l131.dbservice.DBService;

public class ContextListener implements ServletContextListener {
	
	private DBService dbService;

	@Override
	public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
	    dbService = springContext.getBean("dbService", DBService.class);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			dbService.shutdown();
		} catch (SQLException e) {
			
		}
	}

}
