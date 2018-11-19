package ru.otus.l121.servlet;

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
		request.setAttribute("message", "Please enter your user name and password:");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
		dispatcher.forward(request, response);
	}	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("username").isEmpty() || request.getParameter("password").isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/");//request.getServletPath());
		} else {
			request.getSession().setAttribute("username", request.getParameter("username"));
			response.sendRedirect(request.getContextPath() + "/cache-info");//request.getServletPath());
		}
	}	
}
