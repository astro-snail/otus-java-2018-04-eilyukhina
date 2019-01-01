package ru.otus.l161.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l161.message.MessageEvent;
import ru.otus.l161.message.MessageEventListener;
import ru.otus.l161.dataset.*;
import ru.otus.l161.dbmessages.*;
import ru.otus.l161.uiservice.Operation;
import ru.otus.l161.uiservice.UIService;

@SuppressWarnings("serial")
public class UserInfoServlet extends HttpServlet {

	@Autowired
	private UIService uiService;

	@Override
	public void init() throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

				uiService.doRequest(new MsgUserRequestById(id), listener);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (LoginServlet.checkLoggedIn(request, response)) {

			UserDataSet user = UserHelper.getUser(request.getParameterMap());

			AsyncContext asyncContext = request.startAsync(request, response);

			MessageEventListener listener = new MessageEventListener() {

				@Override
				public void messageReceived(MessageEvent event) {
					asyncContext.dispatch("/user-list");
				}
			};

			Operation operation = Operation.valueOf(Operation.class, request.getParameter("operation"));

			switch (operation) {
			case SELECT:
				uiService.doRequest(new MsgUserRequestById(user.getId()), listener);
				break;
			case SAVE:
				uiService.doRequest(new MsgSaveUserRequest(user), listener);
				break;
			case DELETE:
				uiService.doRequest(new MsgDeleteUserRequest(user.getId()), listener);
			}
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