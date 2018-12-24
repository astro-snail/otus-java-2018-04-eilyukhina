package ru.otus.l161;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ru.otus.l161.app.ShutdownMessage;
import ru.otus.l161.cache.CacheConfiguration;
import ru.otus.l161.cache.CacheFactory;
import ru.otus.l161.channel.SocketMessageWorker;
import ru.otus.l161.dbservice.DBService;
import ru.otus.l161.dbservice.DBServiceHibernateImpl;
import ru.otus.l161.message.Address;
import ru.otus.l161.message.Message;
import ru.otus.l161.messages.MsgAllUsersRequest;
import ru.otus.l161.messages.MsgAllUsersResponse;
import ru.otus.l161.messages.MsgCacheParametersRequest;
import ru.otus.l161.messages.MsgCacheParametersResponse;
import ru.otus.l161.messages.MsgDeleteUserRequest;
import ru.otus.l161.messages.MsgDeleteUserResponse;
import ru.otus.l161.messages.MsgSaveUserRequest;
import ru.otus.l161.messages.MsgSaveUserResponse;
import ru.otus.l161.messages.MsgUserRequestById;
import ru.otus.l161.messages.MsgUserRequestByName;
import ru.otus.l161.messages.MsgUserResponse;
import ru.otus.l161.messages.dataset.UserDataSet;

public class DBMain {

	private static final Logger logger = Logger.getLogger(DBMain.class.getName());

    private static final int SERVER_PORT = 5050;
    private static final int LOCAL_PORT = 50200;

    private DBService dbService = new DBServiceHibernateImpl(CacheFactory.getCache(new CacheConfiguration()));
    private SocketMessageWorker worker;
    
    public static void main(String[] args) throws Exception {
        new DBMain().start();
    }

    public void start() throws Exception {
    	
        DBHelper.doWork(dbService);
    	
    	Address server = new Address(SERVER_PORT);
    	Address client = new Address(LOCAL_PORT);
		
        worker = new SocketMessageWorker(server, client);
        worker.init();
        
        Thread executor = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
	                while (!worker.isClosed()) {
	                    Message message = worker.take();
	                    logger.info("Message received: " + message);
	                    
	                    Message response = process(message);
	                    
	                    if (response != null) {
	                    	worker.put(response);
	                    	logger.info("Message sent: " + response);
	                    }	
	                }
	            } catch (SQLException | InterruptedException e) {
	                logger.severe(e.getMessage());
	            }				
			}
		});
        
        executor.start();
        executor.join();
        
        worker.close();
        dbService.shutdown();
    }
    
    private Message process(Message message) throws SQLException {
    	
    	Message response = null;
    	
    	Object payload = message.getPayload();
        
    	if (message.getMessageType() == ShutdownMessage.class) {
        	Thread.currentThread().interrupt();
        }
    	
        if (message.getMessageType() == MsgAllUsersRequest.class) {
        	List<UserDataSet> users = dbService.loadAll();
        	response = new Message(message.getTo(), message.getFrom(), new MsgAllUsersResponse(users));
        }
        
        if (message.getMessageType() == MsgUserRequestById.class) {
        	MsgUserRequestById request = (MsgUserRequestById) payload;
        	UserDataSet user = dbService.load(request.getId());
        	response = new Message(message.getTo(), message.getFrom(), new MsgUserResponse(user));
        }
        
        if (message.getMessageType() == MsgUserRequestByName.class) {
        	MsgUserRequestByName request = (MsgUserRequestByName) payload;
        	UserDataSet user = dbService.loadByName(request.getName());
        	response = new Message(message.getTo(), message.getFrom(), new MsgUserResponse(user));
        }
        
        if (message.getMessageType() == MsgSaveUserRequest.class) {
        	MsgSaveUserRequest request = (MsgSaveUserRequest) payload;
        	UserDataSet user = dbService.save(request.getUser());
        	response = new Message(message.getTo(), message.getFrom(), new MsgSaveUserResponse(user));
        }
        
        if (message.getMessageType() == MsgDeleteUserRequest.class) {
        	MsgDeleteUserRequest request = (MsgDeleteUserRequest) payload;
        	String result = dbService.delete(request.getId());
        	response = new Message(message.getTo(), message.getFrom(), new MsgDeleteUserResponse(result));
        }
        
        if (message.getMessageType() == MsgCacheParametersRequest.class) {
        	Map<String, String> cacheParams = dbService.getCacheParameters();
        	response = new Message(message.getTo(), message.getFrom(), new MsgCacheParametersResponse(cacheParams));
        }
        
        return response;
    }

}
