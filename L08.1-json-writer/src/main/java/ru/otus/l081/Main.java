package ru.otus.l081;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import ru.otus.l081.writer.JsonObjectWriter;

public class Main {

	public static void main(String[] args) {
		Person myPerson = new Person(25, "Jane", "Doe");

		JsonObjectWriter myWriter = new JsonObjectWriter();
		String result = myWriter.writeToJson(myPerson);

		Gson gson = new Gson();
		String gsonResult = gson.toJson(myPerson);

		if (result.equals(gsonResult)) {
			System.out.println("Object is serialised to the same JSON representation");
		} else {
			System.out.println("Object's JSON representations are different");
		}

		System.out.println("JsonObjectWriter:\n" + result);
		System.out.println("Gson:\n" + gsonResult);
	}
}

class Person {
	private static final int MY_CONSTANT = 100;
	private int age;
	private String firstName;
	private String lastName;
	private boolean married = true;
	private transient double salary = 150000.00;
	private Integer testScore = 100;
	private char symbol = 'S';
	List<String> phoneNumbers = new ArrayList<>(Arrays.asList("0987654321", "0123456789", "0147258369"));
	String[] certificates = { "ABAP", "OCA", "OCP" };
	int[][] years = { { 2000, 2008, 2018 }, { 1990, 1991 } };

	public Person(int age, String firstName, String lastName) {
		this.age = age;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	int getAge() {
		return this.age;
	}

	String getFirstName() {
		return this.firstName;
	}

	String getLastName() {
		return this.lastName;
	}
}
