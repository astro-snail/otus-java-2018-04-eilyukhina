package ru.otus.l151.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

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

			// Save user name in session
			request.getSession().setAttribute("username", username);
			response.sendRedirect(request.getContextPath());
		}
	}	
	
	public static boolean checkLoggedIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("username") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}	
		return true;
	}
}
