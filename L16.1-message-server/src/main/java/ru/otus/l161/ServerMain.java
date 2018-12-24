package ru.otus.l161;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import ru.otus.l161.runner.ProcessRunnerImpl;
import ru.otus.l161.server.SocketMessageServer;

public class ServerMain {

	private static final Logger logger = Logger.getLogger(ServerMain.class.getName());
	private static final int CLIENT_START_DELAY_SEC = 5;
        
    public static void main(String[] args) throws Exception {    	
        new ServerMain().start();
    }

    private void start() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Server");
        SocketMessageServer server = new SocketMessageServer();
        mbs.registerMBean(server, name);
        
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
        
        startClients(executorService);
        
        try {
        	server.start();
        } finally {
        	executorService.shutdown();
        	shutdownClients();
        }	
    }

    private void startClients(ScheduledExecutorService executorService) {
    	
    	List<String[]> clientStartCommand = new ArrayList<>();
    
    	clientStartCommand.add(new String[] {"java", "-jar", "../L16.1-db-server/target/db-server.jar"});
    	
    	String source = System.getProperty("user.dir") + "\\..\\L16.1-ui-server\\target\\ui-server.war";
    	String dest = System.getenv("CATALINA_HOME") + "\\webapps";
    	
    	clientStartCommand.add(new String[] {"CMD", "/C", "COPY", "/Y", source, dest});
    	clientStartCommand.add(new String[] {System.getenv("CATALINA_HOME") + "/bin/startup.bat"});

    	for (String[] command : clientStartCommand) {
    		executorService.schedule(() -> {
    			try {
    				new ProcessRunnerImpl().start(command);
    				logger.log(Level.INFO, "Client started");
    			} catch (IOException e) {
    				logger.log(Level.SEVERE, e.getMessage());
    			}
    		}, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
    	}
    }

    private void shutdownClients() {

    	List<String[]> clientStopCommand = new ArrayList<>();
    	
    	clientStopCommand.add(new String[] {System.getenv("CATALINA_HOME") + "/bin/shutdown.bat"});
    	
    	for (String[] command : clientStopCommand) {
    		new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
	    				new ProcessRunnerImpl().start(command);
	    				logger.log(Level.INFO, "Client stopped");
	    			} catch (IOException e) {
	    				logger.log(Level.SEVERE, e.getMessage());
	    			}					
				}
			}).start();
    	}	
    }
}
