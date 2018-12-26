package ru.otus.l061;

import java.util.Scanner;

import ru.otus.l061.atm.ATM;
import ru.otus.l061.denomination.Denomination;

public class Main {

	public static void main(String[] args) {
		ATM atm = new ATM();
		System.out.println("Current balance: " + atm.getBalance());

		String operation = "";

		try (Scanner input = new Scanner(System.in)) {

			while (!"x".equals(operation.toLowerCase())) {

				System.out.println("Enter operation: '+' - deposit, '-' - withdraw, 'b' - balance, 'x' - exit");
				operation = input.nextLine();

				switch (operation.toLowerCase()) {
				case "+": {
					System.out.println(
							"Enter denomination (5000, 2000, 1000, 500, 200, 100) and number of banknotes to deposit");
					int nominal = Integer.parseInt(input.nextLine());
					int count = Integer.parseInt(input.nextLine());
					atm.accept(Denomination.getFromNominal(nominal), count);
					break;
				}

				case "-": {
					System.out.println("Enter amount to withdraw");
					int amount = Integer.parseInt(input.nextLine());
					atm.dispense(amount);
					break;
				}

				case "b": {
					System.out.println("Current balance: " + atm.getBalance());
					break;
				}
				}
			}
		} catch (Exception e) {
		}

		// atm.dispense(57500);
		// System.out.println("Current balance: " + atm.getBalance());
		//
		// atm.dispense(21700);
		// System.out.println("Current balance: " + atm.getBalance());
		//
		// atm.dispense(8800);
		// System.out.println("Current balance: " + atm.getBalance());
		//
		// atm.accept(Denomination.ONE_THOUSAND, 3);
		// System.out.println("Current balance: " + atm.getBalance());
		//
		// atm.accept(Denomination.FIVE_HUNDRED, 2);
		// System.out.println("Current balance: " + atm.getBalance());
		//
		// atm.dispense(4000);
		// System.out.println("Current balance: " + atm.getBalance());

	}

}
