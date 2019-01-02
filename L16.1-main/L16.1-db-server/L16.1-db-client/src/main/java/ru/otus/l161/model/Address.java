package ru.otus.l161.model;

public class Address {

	private Long id;
	private String street;

	public Address() {

	}

	public Address(Long id, String street) {
		this.setId(id);
		this.setStreet(street);
	}

	public Address(String street) {
		this(null, street);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public String toString() {
		return "Address ID = " + getId() + ", street = " + getStreet();
	}
}
