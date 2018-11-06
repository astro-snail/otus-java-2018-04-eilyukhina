package ru.otus.l091.dataset;

public class UserDataSet extends DataSet {
	private String name;
	private int age;

	public UserDataSet() {
		super();
	}

	public UserDataSet(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

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
		return "ID = " + getId() + ", name = " + getName() + ", age = " + getAge();
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
