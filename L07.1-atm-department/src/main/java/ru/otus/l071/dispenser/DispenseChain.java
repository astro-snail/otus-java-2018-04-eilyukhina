package ru.otus.l071.dispenser;

import java.util.Map;

import ru.otus.l071.denomination.Denomination;

public interface DispenseChain {

	public void setNext(DispenseChain next);

	public void dispense(int amountRequested, Map<Denomination, Integer> banknotesToDispense);

}
