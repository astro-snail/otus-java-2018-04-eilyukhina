package ru.otus.l071.atm;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ru.otus.l071.composite.Component;
import ru.otus.l071.denomination.Denomination;
import ru.otus.l071.denomination.DenominationComparator;
import ru.otus.l071.department.EventType;
import ru.otus.l071.dispenser.CashDispenser;
import ru.otus.l071.dispenser.DispenseChain;
import ru.otus.l071.observer.Event;

public class ATM implements Component {

	public static final int BANKNOTES_INITIAL_COUNT = 10;
	
	private final int id; 
	private Map<Denomination, Integer> banknotes = new TreeMap<>(new DenominationComparator());
	private Memento initialState;
	
	public ATM(int id) {
		this(id, BANKNOTES_INITIAL_COUNT);
	}
	
	public ATM(int id, int initialCount, Denomination ... availableDenominations) {
		this.id = id;
		
		Denomination[] denominations = null;
		
		if (availableDenominations == null || availableDenominations.length == 0) {
			denominations = Denomination.values();
		} else {
			denominations = availableDenominations;
		}
		
		for (Denomination denomination : denominations) {
			banknotes.put(denomination, initialCount);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * 1 + id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;
		return id == ((ATM) obj).getId();
	}
	
	@Override
	public int getBalance() {
		return calculateAmount(banknotes);
	}
	
	private void restore() {
		if (initialState != null) {
			restoreState(initialState);
		}
	}
	
	private void save() {
		initialState = saveState();
	}	
	
	private DispenseChain getDispenseChain() {
		DispenseChain chain = null;
				
		List<Map.Entry<Denomination, Integer>> availableDenominations = banknotes.entrySet().stream()
				.filter(entry -> entry.getValue() > 0)
				.collect(Collectors.toList());
		
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

	private static int calculateAmount(Map<Denomination, Integer> banknotes) {
		return banknotes.entrySet().stream()
			.mapToInt(entry -> entry.getValue() * entry.getKey().getNominal())
			.sum();
	}
	
	private static void print(Map<Denomination, Integer> banknotes) {
		banknotes.forEach((denomination, count) -> print(denomination, count));
		System.out.println("Total amount: " + calculateAmount(banknotes));
	}
	
	private static void print(Denomination denomination, int count) {
		System.out.println(denomination.getNominal() + " X " + count + " = " + denomination.getNominal() * count);
	}
	
	public Map<Denomination, Integer> dispense(int amount) {
		DispenseChain chain = getDispenseChain();
		Map<Denomination, Integer> banknotesToDispense = new TreeMap<>(new DenominationComparator());
				
		if (chain == null || amount <= 0 || amount > calculateAmount(banknotes)) {
			throw new RuntimeException("Cannot dispense this amount: " + amount);
		}
		
		chain.dispense(amount, banknotesToDispense);
		if (amount != calculateAmount(banknotesToDispense)) {
			throw new RuntimeException("Cannot dispense this amount: " + amount);
		}

		banknotesToDispense.forEach((denomination, count) -> banknotes.merge(denomination, count, (oldCount, newCount) -> oldCount - newCount));
		print(banknotesToDispense);
		
		return banknotesToDispense;
	}
	
	public void accept(Denomination denomination, int count) {
		if (denomination == null) {
			throw new RuntimeException("Unknown denomination");
		}
		
		if (count <= 0) {
			throw new RuntimeException("Number of banknotes should be positive");
		}

		banknotes.merge(denomination, count, (oldCount, newCount) -> oldCount + newCount);
		print(denomination, count);
	}
	
	public class Memento {
		private final Map<Denomination, Integer> banknotes;
		
		private Memento(Map<Denomination, Integer> banknotes) {
			this.banknotes = new TreeMap<>(banknotes);
		}	
		
		private Map<Denomination, Integer> getBanknotes() {
			return new TreeMap<>(banknotes);
		}
	}
	
	public Memento saveState() {
		return new Memento(this.banknotes);
	}
	
	public void restoreState(Memento snapshot) {
		this.banknotes = snapshot.getBanknotes();
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "ATM " + getId() + ", current balance: " + getBalance();
	}
	
	@Override
	public void print() {
		System.out.println(this);
	}

	@Override
	public void handleEvent(Event event) {
		String e = event.getEvent();
		
		System.out.println("Handling event " + e);
		
		if (EventType.ADD.getEvent().equals(e)) { 
			save();
		} else if (EventType.RESTORE.getEvent().equals(e)) {
			restore();
		}
	}
	
}
