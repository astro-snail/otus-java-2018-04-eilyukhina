package ru.otus.l151.servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l151.DBHelper;
import ru.otus.l151.app.DBService;
import ru.otus.l151.cache.Cache;

@SuppressWarnings("serial")
public class CacheInfoServlet extends HttpServlet {
	
	@Autowired
	private DBService dbService;
	
	@Autowired
	private Cache cache;
	
	/*
	 * It is also possible to access Spring context directly, e.g:
	 * 
	 * WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	 * dbService = springContext.getBean("dbService", DBService.class);
	 * cache = springContext.getBean("cache", Cache.class);
	 */
	@Override
	public void init() throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
	}
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// If user has not logged in - redirect to login page
		if (request.getSession().getAttribute("username") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else {
			final AsyncContext asyncContext = request.startAsync(request, response);
			asyncContext.start(new Runnable() {
				@Override
				public void run() {
					request.setAttribute("maxCapacity", cache.getProperties().get("maxCapacity"));
					request.setAttribute("lifeTime", cache.getProperties().get("lifeTime"));
					request.setAttribute("idleTime", cache.getProperties().get("idleTime"));
					request.setAttribute("isEternal", cache.getProperties().get("isEternal"));
					request.setAttribute("cacheSize", cache.getSize());
					request.setAttribute("hitCount", cache.getHitCount());
					request.setAttribute("missCount", cache.getMissCount());
					
					asyncContext.dispatch("/cache-info.jsp");
				}
			});	
		}
	}	
}
