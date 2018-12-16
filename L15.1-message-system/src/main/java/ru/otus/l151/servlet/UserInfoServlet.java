package ru.otus.l151.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l151.dataset.AddressDataSet;
import ru.otus.l151.dataset.PhoneDataSet;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.MessageContext;
import ru.otus.l151.messagesystem.MessageEvent;
import ru.otus.l151.messagesystem.MessageEventListener;
import ru.otus.l151.uiservice.Operation;
import ru.otus.l151.uiservice.UIService;

@SuppressWarnings("serial")
public class UserInfoServlet extends HttpServlet {
	
	@Autowired
	private UIService uiService;
	
	@Override
	public void init() throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
	}
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (LoginServlet.checkLoggedIn(request, response)) {	
			
			Long id = UserHelper.getId(request.getParameter("userId"));
			Operation operation = Operation.valueOf(Operation.class, request.getParameter("operation"));
			
			if (id == null) {
				// Do not start asynchronous processing
				request.setAttribute("operation", operation);
				request.setAttribute("user", UserHelper.createUser());
				request.getRequestDispatcher("/user-info.jsp").forward(request, response);
			} else {
				AsyncContext asyncContext = request.startAsync(request, response);	
				
				MessageEventListener listener = new MessageEventListener() {
				
					@Override
					public void messageReceived(MessageEvent event) {
						asyncContext.getRequest().setAttribute("operation", operation);
						asyncContext.getRequest().setAttribute("user", event.getValue());
						asyncContext.dispatch("/user-info.jsp");
					}
				};
			
				MessageContext messageContext = new MessageContext();
				messageContext.addListener(listener);
				uiService.handleUserRequest(messageContext, id);
			}
		}	
	}	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (LoginServlet.checkLoggedIn(request, response)) {
			
			UserDataSet user = UserHelper.getUser(request.getParameterMap());
			Operation operation = Operation.valueOf(Operation.class, request.getParameter("operation"));
			
			AsyncContext asyncContext = request.startAsync(request, response);			
			
			MessageEventListener listener = new MessageEventListener() {
				
				@Override
				public void messageReceived(MessageEvent event) {
					asyncContext.dispatch("/user-list");
				}
			};
			
			MessageContext messageContext = new MessageContext();
			messageContext.addListener(listener);
			uiService.handleUserRequest(messageContext, operation, user);
		}	
	}	
}

class UserHelper {
	
	static UserDataSet getUser(Map<String, String[]> parameters) {
		Long userId = getId(parameters.get("userId")[0]);
		String name = parameters.get("name")[0];
		int age = Integer.parseInt(parameters.get("age")[0]);
		UserDataSet user = new UserDataSet(userId, name, age);
	
		Long addressId = getId(parameters.get("addressId")[0]);
		String street = parameters.get("address")[0];
		user.setAddress(new AddressDataSet(addressId, street));
	
		String[] phoneIds = parameters.get("phoneId");
		String[] phoneNumbers = parameters.get("phone");
		
		for (int i = 0; i < phoneNumbers.length; i++) {
			user.addPhone(new PhoneDataSet(getId(phoneIds[i]), phoneNumbers[i]));
		}
		
		return user;
	}

	static Long getId(String parameter) {
		return parameter == null || parameter.isEmpty() ? null : Long.valueOf(parameter);
	}
	
	static UserDataSet createUser() {
		UserDataSet user = new UserDataSet();
		user.setAddress(new AddressDataSet());
		user.addPhone(new PhoneDataSet());
		return user;
	}
}	