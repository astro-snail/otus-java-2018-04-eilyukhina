package ru.otus.l061.atm;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ru.otus.l061.denomination.Denomination;
import ru.otus.l061.denomination.DenominationComparator;
import ru.otus.l061.dispenser.CashDispenser;
import ru.otus.l061.dispenser.DispenseChain;

public class ATM {
	public static final int BANKNOTES_INITIAL_COUNT = 10;

	private Map<Denomination, Integer> banknotes = new TreeMap<>(new DenominationComparator());

	public ATM() {
		initialise();
	}

	private void initialise() {
		for (Denomination denomination : Denomination.values()) {
			banknotes.put(denomination, BANKNOTES_INITIAL_COUNT);
		}
	}

	public int getBalance() {
		return calculateAmount(banknotes);
	}

	private DispenseChain getDispenseChain() {
		DispenseChain chain = null;

		List<Map.Entry<Denomination, Integer>> availableDenominations = banknotes.entrySet().stream()
				.filter(entry -> entry.getValue() > 0).collect(Collectors.toList());

		Iterator<Map.Entry<Denomination, Integer>> iterator = availableDenominations.iterator();

		if (iterator.hasNext()) {
			Map.Entry<Denomination, Integer> denomination = iterator.next();
			chain = new CashDispenser(denomination.getKey(), denomination.getValue());

			DispenseChain c1 = chain;

			while (iterator.hasNext()) {
				denomination = iterator.next();
				DispenseChain c2 = new CashDispenser(denomination.getKey(), denomination.getValue());
				c1.setNext(c2);
				c1 = c2;
			}
		}

		return chain;
	}

	private int calculateAmount(Map<Denomination, Integer> banknotes) {
		return banknotes.entrySet().stream().mapToInt(entry -> entry.getValue() * entry.getKey().getNominal()).sum();
	}

	private void print(Map<Denomination, Integer> banknotes) {
		banknotes.forEach((denomination, count) -> System.out.println(denomination.getNominal() + " X " + count));
	}

	public Map<Denomination, Integer> dispense(int amount) {
		DispenseChain chain = getDispenseChain();
		Map<Denomination, Integer> banknotesToDispense = new TreeMap<>(new DenominationComparator());

		if (chain == null || amount <= 0 || amount > calculateAmount(banknotes)) {
			System.out.println("Cannot dispense this amount: " + amount);
		} else {
			chain.dispense(amount, banknotesToDispense);

			if (amount != calculateAmount(banknotesToDispense)) {
				System.out.println("Cannot dispense this amount: " + amount);
			} else {
				banknotesToDispense.forEach((denomination, count) -> banknotes.merge(denomination, count,
						(oldCount, newCount) -> oldCount - newCount));

				System.out.println("Dispensed: " + amount);
				print(banknotesToDispense);
			}
		}

		return banknotesToDispense;
	}

	public void accept(Denomination denomination, int count) {
		if (denomination == null) {
			System.out.println("Unknown denomination");
			return;
		}

		if (count <= 0) {
			System.out.println("Number of banknotes should be positive");
			return;
		}

		banknotes.merge(denomination, count, (oldCount, newCount) -> oldCount + newCount);

		System.out.println(
				"Accepted: " + denomination.getNominal() + " X " + count + " = " + denomination.getNominal() * count);
	}
}
