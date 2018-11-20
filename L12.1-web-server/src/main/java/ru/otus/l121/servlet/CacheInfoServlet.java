package ru.otus.l121.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.otus.l121.DBHelper;

@SuppressWarnings("serial")
public class CacheInfoServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// If user has not logged in - redirect to login page
		if (request.getSession().getAttribute("username") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else {
			request.setAttribute("maxCapacity", DBHelper.getCache().getProperties().get("maxCapacity"));
			request.setAttribute("lifeTime", DBHelper.getCache().getProperties().get("lifeTime"));
			request.setAttribute("idleTime", DBHelper.getCache().getProperties().get("idleTime"));
			request.setAttribute("isEternal", DBHelper.getCache().getProperties().get("isEternal"));
			request.setAttribute("cacheSize", DBHelper.getCache().getSize());
			request.setAttribute("hitCount", DBHelper.getCache().getHitCount());
			request.setAttribute("missCount", DBHelper.getCache().getMissCount());
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/cache-info.jsp");
			dispatcher.forward(request, response);
		}
	}	
}
