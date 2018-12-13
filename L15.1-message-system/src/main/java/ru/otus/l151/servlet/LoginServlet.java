package ru.otus.l151.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l151.app.DBService;
import ru.otus.l151.app.UIService;
import ru.otus.l151.app.MessageSystemContext;
import ru.otus.l151.dataset.AddressDataSet;
import ru.otus.l151.dataset.PhoneDataSet;
import ru.otus.l151.dataset.UserDataSet;
import ru.otus.l151.messagesystem.Address;
import ru.otus.l151.messagesystem.MessageSystem;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
		dispatcher.forward(request, response);
	}	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (username.isEmpty() || password.isEmpty()) {
			request.setAttribute("message", "Your username or password are incorrect. Please try again.");
			doGet(request, response);
		} else {
			// User authentication should be here - just assume that it is always successful

			// Save username in session
			request.getSession().setAttribute("username", username);
			response.sendRedirect(request.getContextPath() + "/admin.html");
		}
	}	
}
