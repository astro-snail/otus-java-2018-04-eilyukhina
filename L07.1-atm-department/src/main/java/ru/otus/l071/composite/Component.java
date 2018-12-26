package ru.otus.l071.composite;

import ru.otus.l071.observer.Observer;

public interface Component extends Observer {

	public int getBalance();

	public void print();

}
