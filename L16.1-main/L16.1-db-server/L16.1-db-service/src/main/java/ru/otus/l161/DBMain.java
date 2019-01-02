package ru.otus.l161;

import ru.otus.l161.dbservice.RequestHandlerImpl;
import ru.otus.l161.handler.RequestHandler;

public class DBMain {

	public static void main(String[] args) throws Exception {
		new DBMain().start();
	}

	public void start() throws Exception {
		RequestHandler handler = new RequestHandlerImpl();
		handler.init();
	}
}
