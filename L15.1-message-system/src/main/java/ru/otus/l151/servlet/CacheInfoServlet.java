package ru.otus.l151.servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.otus.l151.uiservice.UIService;

@SuppressWarnings("serial")
public class CacheInfoServlet extends HttpServlet {
	
	@Autowired
	private UIService uiService;
	
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
		if (LoginServlet.checkLoggedIn(request, response)) {
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
						request.getRequestDispatcher("/cache-info.jsp").forward(request, response);
					} catch (ServletException e) {
						throw new RuntimeException(e);
					}
				}
			};
			
			AsyncContext asyncContext = request.startAsync(request, response);
			asyncContext.addListener(listener);
			uiService.handleCacheRequest(asyncContext);
		}
	}	
}
