package ru.otus.l161.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l161.uiservice.UIService;

public class ContextListener implements ServletContextListener {
	
	@Autowired
	private UIService uiService;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, event.getServletContext());
	    uiService.init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {			
		uiService.shutdown();
	}

}
