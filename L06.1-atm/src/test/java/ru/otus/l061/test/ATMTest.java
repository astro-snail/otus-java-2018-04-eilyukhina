package ru.otus.l061.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.otus.l061.atm.ATM;
import ru.otus.l061.denomination.Denomination;

class ATMTest {

	ATM atm;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		atm = new ATM();
	}

	@AfterEach
	void tearDown() throws Exception {
		atm = null;
	}

	@Test
	void testGetBalance() {
		int expectedBalance = 0;
		for (Denomination denomination : Denomination.values()) {
			expectedBalance += denomination.getNominal() * ATM.BANKNOTES_INITIAL_COUNT;
		}
		assertEquals(expectedBalance, atm.getBalance());

		atm.dispense(atm.getBalance());
		assertEquals(0, atm.getBalance());
	}

	@Test
	void testDispense() {
		int amount = 3400;

		int balanceBefore = atm.getBalance();
		Map<Denomination, Integer> banknotesToDispense = atm.dispense(amount);
		int balanceAfter = atm.getBalance();

		assertEquals(balanceBefore, balanceAfter + amount);

		assertTrue(banknotesToDispense.containsKey(Denomination.TWO_THOUSAND));
		int count = banknotesToDispense.get(Denomination.TWO_THOUSAND);
		assertEquals(1, count);

		assertTrue(banknotesToDispense.containsKey(Denomination.ONE_THOUSAND));
		count = banknotesToDispense.get(Denomination.ONE_THOUSAND);
		assertEquals(1, count);

		assertTrue(banknotesToDispense.containsKey(Denomination.TWO_HUNDRED));
		count = banknotesToDispense.get(Denomination.TWO_HUNDRED);
		assertEquals(2, count);
	}

	@Test
	void testAccept() {
		for (Denomination denomination : Denomination.values()) {
			for (int count = 0; count < 10; count++) {
				int balanceBefore = atm.getBalance();
				atm.accept(denomination, count);
				int balanceAfter = atm.getBalance();

				assertEquals(balanceBefore + denomination.getNominal() * count, balanceAfter);
			}
		}
	}

}
