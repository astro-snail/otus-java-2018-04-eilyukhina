package ru.otus.l161.model;

public class Phone {

	private Long id;
	private String number;

	public Phone() {

	}

	public Phone(Long id, String number) {
		this.setId(id);
		this.setNumber(number);
	}

	public Phone(String number) {
		this(null, number);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Phone ID = " + getId() + ", number = " + getNumber();
	}
}
