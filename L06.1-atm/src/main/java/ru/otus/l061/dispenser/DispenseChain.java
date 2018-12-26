package ru.otus.l061.dispenser;

import java.util.Map;

import ru.otus.l061.denomination.Denomination;

public interface DispenseChain {

	public void setNext(DispenseChain next);

	public void dispense(int amountRequested, Map<Denomination, Integer> banknotesToDispense);

}
