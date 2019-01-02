package ru.otus.l161.model;

import java.util.ArrayList;
import java.util.List;

public class User {

	private Long id;
	private String name;
	private int age;
	private Address address;
	private List<Phone> phones = new ArrayList<>();

	public User() {

	}

	public User(Long id, String name, int age) {
		this.setId(id);
		this.setName(name);
		this.setAge(age);
	}

	public User(String name, int age) {
		this(null, name, age);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public void addPhone(Phone phone) {
		phones.add(phone);
	}

	public void removePhone(Phone phone) {
		phones.remove(phone);
	}

	@Override
	public String toString() {
		return "User ID = " + getId() + ", name = " + getName() + ", age = " + getAge() + ", address = " + getAddress()
				+ ", phones = " + getPhones();
	}
}
