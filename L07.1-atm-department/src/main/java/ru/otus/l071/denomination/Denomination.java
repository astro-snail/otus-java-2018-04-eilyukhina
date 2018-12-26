package ru.otus.l071.denomination;

public enum Denomination {

	FIVE_THOUSAND(5000), TWO_THOUSAND(2000), ONE_THOUSAND(1000), FIVE_HUNDRED(500), TWO_HUNDRED(200), ONE_HUNDRED(100);

	private int nominal;

	private Denomination(int value) {
		this.nominal = value;
	}

	public int getNominal() {
		return nominal;
	}

	public static Denomination getFromNominal(int nominal) {
		switch (nominal) {
		case 5000:
			return FIVE_THOUSAND;
		case 2000:
			return TWO_THOUSAND;
		case 1000:
			return ONE_THOUSAND;
		case 500:
			return FIVE_HUNDRED;
		case 200:
			return TWO_HUNDRED;
		case 100:
			return ONE_HUNDRED;
		default:
			return null;
		}
	}
}
