package ru.otus.l061.denomination;

import java.util.Comparator;

public class DenominationComparator implements Comparator<Denomination> {

	@Override
	public int compare(Denomination o1, Denomination o2) {
		return o2.getNominal() - o1.getNominal();
	}
}
