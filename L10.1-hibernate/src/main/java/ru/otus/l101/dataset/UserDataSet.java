package ru.otus.l101.dataset;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserDataSet extends DataSet {
	
	private String name;
	private int age;
	private AddressDataSet address;
	private List<PhoneDataSet> phones;

	public UserDataSet() {
		super();
	}

	public UserDataSet(Long id, String name, int age, AddressDataSet address, List<PhoneDataSet> phones) {
		this.setId(id);
		this.setName(name);
		this.setAge(age);
		this.setAddress(address);
		this.setPhones(phones);
	}
	
	public UserDataSet(String name, int age, AddressDataSet address, List<PhoneDataSet> phones) {
		this(null, name, age, address, phones);
	}

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	public AddressDataSet getAddress() {
		return address;
	}

	public void setAddress(AddressDataSet address) {
		this.address = address;
		address.setUser(this);
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	public List<PhoneDataSet> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneDataSet> phones) {
		this.phones = phones;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	@Column(name = "age")
	public int getAge() {
		return age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User ID = " + getId() + 
				", name = " + getName() + 
				", age = " + getAge() + 
				", address = " + getAddress() +
				", phones = " + getPhones();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UserDataSet)) {
			return false;
		}
		UserDataSet other = (UserDataSet)obj;
		return getId() == other.getId() && name.equals(other.name) && age == other.age;
	}
}

