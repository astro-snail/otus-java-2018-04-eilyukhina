package ru.otus.l121;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.tomcat.util.scan.StandardJarScanner;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

import ru.otus.l121.servlet.*;

public class Main {
	
	public static void main(String[] args) throws Exception {

        /*Server server = new Server(8080);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(MyServlet.class, "/*");

        server.start();

        server.join();*/
		
		Server server = new Server(8080);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	    context.setResourceBase("html");
	    context.setContextPath("/");
    
	    File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");
    
        if (!scratchDir.exists()) {
            if (!scratchDir.mkdirs()) {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }
        context.setAttribute("javax.servlet.context.tempdir", scratchDir);
    
        // Set Classloader of Context to be sane (needed for JSTL)
        // JSP requires a non-System classloader, this simply wraps the
        // embedded System classloader in a way that makes it suitable
        // for JSP to use
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], Main.class.getClassLoader());
        context.setClassLoader(jspClassLoader);
        
        // Manually call JettyJasperInitializer on context startup
        context.addBean(new JspStarter(context));
        
        // Create / Register JSP Servlet (must be named "jsp" per spec)
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.8");
        holderJsp.setInitParameter("compilerSourceVM", "1.8");
        holderJsp.setInitParameter("keepgenerated", "true");
        context.addServlet(holderJsp, "*.jsp");
        
        //TemplateProcessor templateProcessor = new TemplateProcessor();

        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(CacheInfoServlet.class, "/cache-info");
        context.addServlet(DefaultServlet.class, "/");
        //context.addServlet(AdminServlet.class, "/admin");
        //context.addServlet(TimerServlet.class, "/timer");

        //Server server = new Server(PORT);
        server.setHandler(context);//new HandlerList(resourceHandler, context));
        server.start();
        server.join();
	}
}

class JspStarter extends AbstractLifeCycle implements ServletContextHandler.ServletContainerInitializerCaller {
    
	JettyJasperInitializer initializer;
    ServletContextHandler context;
    
    public JspStarter (ServletContextHandler context) {
        this.initializer = new JettyJasperInitializer();
        this.context = context;
        //this.context.setAttribute("org.apache.tomcat.JarScanner", new StandardJarScanner());
    }

    @Override
    protected void doStart() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(context.getClassLoader());
        try {
            initializer.onStartup(null, context.getServletContext());   
            super.doStart();
        } finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }
    }
}