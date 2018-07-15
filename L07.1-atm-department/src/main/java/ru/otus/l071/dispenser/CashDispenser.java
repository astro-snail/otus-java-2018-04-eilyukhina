package ru.otus.l071.dispenser;

import java.util.Map;

import ru.otus.l071.denomination.Denomination;

public class CashDispenser implements DispenseChain {
	
	private Denomination denomination;
	private int count;
	
	private DispenseChain next;
	
	public CashDispenser(Denomination denomination, int count) {
		this.denomination = denomination;
		this.count = count;
	}

	@Override
	public void setNext(DispenseChain next) {
		this.next = next;
	}

	@Override
	public void dispense(int amountRequested, Map<Denomination, Integer> banknotesToDispense) {
		if (amountRequested < denomination.getNominal() || count <= 0) {
			if (next != null) {
				next.dispense(amountRequested, banknotesToDispense);
			}	
		} else {
			int dispenseCount = Math.min(count, amountRequested / denomination.getNominal());
			int remainder = amountRequested - dispenseCount * denomination.getNominal();
			
			banknotesToDispense.put(denomination, dispenseCount);
			
			if (remainder > 0 && next != null) {
				next.dispense(remainder, banknotesToDispense);
			}
		}
	}
}
