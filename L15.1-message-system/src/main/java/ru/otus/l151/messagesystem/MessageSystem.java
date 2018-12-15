package ru.otus.l151.messagesystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSystem {
	
	private static final Logger logger = Logger.getLogger(MessageSystem.class.getName());
	
	private final Map<Address, Addressee> addresses;
	private final Map<Address, LinkedBlockingQueue<Message>> messages;
	private final List<Thread> workers;
	
	public MessageSystem() {
		this.workers = new ArrayList<>();
		this.addresses = new HashMap<>();
		this.messages = new HashMap<>();
	}
	
	public Address registerAddressee(Addressee addressee) {
		return registerAddressee(addressee, new Address());
	}
	
	public Address registerAddressee(Addressee addressee, Address address) {
		addresses.put(address, addressee);
		messages.put(address, new LinkedBlockingQueue<>());
		return address;
	}
	
	public void sendMessage(Message message) {
		messages.get(message.getTo()).add(message);
	}

	public void start() {
		for (Map.Entry<Address, Addressee> entry : addresses.entrySet()) {
			String name = "MS-worker-" + entry.getKey().getId();
			Thread thread = new Thread(() -> {
				LinkedBlockingQueue<Message> queue = messages.get(entry.getKey());
				while (true) {
					try {
						Message message = queue.take();
						logger.log(Level.INFO, "Message retrieved. " + message.getClass());
						message.exec(entry.getValue());
					} catch (MessageException e) {
						logger.log(Level.WARNING, "Error in message processing. " + e.getMessage());
					} catch (InterruptedException e) {
						logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
						return;
					}	
				}			
			});
			thread.setName(name);
			thread.start();
			workers.add(thread);
		}	
	}
	
	public void dispose() {
		workers.forEach(Thread::interrupt);
	}
}
