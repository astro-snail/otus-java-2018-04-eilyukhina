package ru.otus.l161.channel;

import java.io.Closeable;

import ru.otus.l161.message.Message;

public interface MessageWorker extends Closeable {

	void put(Message message) throws InterruptedException;

	Message poll();

	Message take() throws InterruptedException;

}
