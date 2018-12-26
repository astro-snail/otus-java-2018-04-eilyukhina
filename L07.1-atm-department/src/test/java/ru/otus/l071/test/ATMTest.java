package ru.otus.l071.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.l071.atm.ATM;
import ru.otus.l071.composite.Component;
import ru.otus.l071.denomination.Denomination;
import ru.otus.l071.department.ATMDepartment;

class ATMTest {

	ATMDepartment department;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		department = new ATMDepartment("ATM Department");

		ATM atm1 = new ATM(1, 5, Denomination.ONE_THOUSAND);
		department.add(atm1);

		ATM atm2 = new ATM(2, 10, Denomination.FIVE_HUNDRED);
		department.add(atm2);

		ATM atm3 = new ATM(3, 10, Denomination.FIVE_THOUSAND);
		department.add(atm3);
	}

	@AfterEach
	void tearDown() throws Exception {
		department = null;
	}

	@Test
	void testGetBalance() {
		int expectedBalance = 0;
		for (Component atm : department.getChildren()) {
			expectedBalance += atm.getBalance();
		}
		assertEquals(expectedBalance, department.getBalance());
	}

	@Test
	void testRestore() {
		int expectedBalance = department.getBalance();

		for (Component atm : department.getChildren()) {
			try {
				((ATM) atm).dispense(5000);
			} catch (RuntimeException e) {
			}
		}

		assertNotEquals(expectedBalance, department.getBalance());

		department.restore();

		assertEquals(expectedBalance, department.getBalance());
	}
}
