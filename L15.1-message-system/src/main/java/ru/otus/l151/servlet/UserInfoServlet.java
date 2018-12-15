package ru.otus.l151.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l151.dataset.AddressDataSet;
import ru.otus.l151.dataset.PhoneDataSet;
import ru.otus.l151.dataset.UserDataSet;
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
			Long id = getId(request.getParameter("userId"));
			Operation operation = Operation.valueOf(request.getParameter("operation"));
			
			AsyncListener listener = new AsyncListener() {
				@Override
				public void onTimeout(AsyncEvent event) throws IOException {
					
				}
				
				@Override
				public void onStartAsync(AsyncEvent event) throws IOException {
				
				}
				
				@Override
				public void onError(AsyncEvent event) throws IOException {
					
				}
				
				@Override
				public void onComplete(AsyncEvent event) throws IOException {
					HttpServletRequest request = (HttpServletRequest)event.getSuppliedRequest();
					HttpServletResponse response = (HttpServletResponse)event.getSuppliedResponse();					
					try {
						request.getRequestDispatcher("/user-info.jsp").forward(request, response);
					} catch (ServletException e) {
						throw new RuntimeException(e);
					}
				}
			};
			
			AsyncContext asyncContext = request.startAsync(request, response);
			asyncContext.addListener(listener);
			request.setAttribute("operation", operation);
			if (id != null) {
				uiService.handleUserRequest(asyncContext, id);
			} else {
				request.setAttribute("user", createUser());
				asyncContext.complete();
			}
		}	
	}	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (LoginServlet.checkLoggedIn(request, response)) {
			UserDataSet user = getUser(request.getParameterMap());
			Operation operation = Operation.valueOf(request.getParameter("operation"));
			
			AsyncListener listener = new AsyncListener() {
				@Override
				public void onTimeout(AsyncEvent event) throws IOException {						
				
				}
				
				@Override
				public void onStartAsync(AsyncEvent event) throws IOException {
				
				}
				
				@Override
				public void onError(AsyncEvent event) throws IOException {
					
				}
				
				@Override
				public void onComplete(AsyncEvent event) throws IOException {
					HttpServletRequest request = (HttpServletRequest)event.getSuppliedRequest();
					HttpServletResponse response = (HttpServletResponse)event.getSuppliedResponse();
					response.sendRedirect(request.getContextPath() + "/user-list");
				}
			};
			AsyncContext asyncContext = request.startAsync(request, response);
			asyncContext.addListener(listener);
			uiService.handleUserRequest(asyncContext, operation, user);
		}	
	}	
	
	private UserDataSet getUser(Map<String, String[]> parameters) {
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
	
	private Long getId(String parameter) {
		return parameter.isEmpty() ? null : Long.valueOf(parameter);
	}
	
	private UserDataSet createUser() {
		UserDataSet user = new UserDataSet();
		user.setAddress(new AddressDataSet());
		user.addPhone(new PhoneDataSet());
		return user;
	}
}
