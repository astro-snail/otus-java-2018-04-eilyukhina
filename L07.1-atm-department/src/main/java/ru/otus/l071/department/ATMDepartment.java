package ru.otus.l071.department;

import java.util.ArrayList;
import java.util.List;

import ru.otus.l071.composite.Component;
import ru.otus.l071.observer.Event;

public class ATMDepartment implements Component {

	private String name;
	private List<Component> children = new ArrayList<>();
	
	public List<Component> getChildren() {
		return children;
	}

	public ATMDepartment(String name) {
		this.name = name;
	}
	
	public void add(Component atm) {
		if (!children.contains(atm)) {
			children.add(atm);
			atm.handleEvent(() -> EventType.ADD.getEvent());
		}
	}
		
	public void remove(Component atm) {
		children.remove(atm);
		atm.handleEvent(() -> EventType.REMOVE.getEvent());
	}

	public void restore() {
		for (Component atm : children) {
			atm.handleEvent(() -> EventType.RESTORE.getEvent());
		}
	}
	
	@Override
	public int getBalance() {
		int totalBalance = 0;
		for (Component atm : children) {
			totalBalance += atm.getBalance(); 
		}
		return totalBalance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public void print() {
		System.out.println(this);
		for (Component atm : children) {
			atm.print();
		}
		System.out.println("Total balance: " + getBalance());
	}

	@Override
	public void handleEvent(Event event) {
		if (EventType.RESTORE.getEvent().equals(event.getEvent())) {
			restore();
		}
	}
}
