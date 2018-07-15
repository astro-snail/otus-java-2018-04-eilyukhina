package ru.otus.l071;

import ru.otus.l071.atm.ATM;
import ru.otus.l071.denomination.Denomination;
import ru.otus.l071.department.ATMDepartment;

public class Main {

	public static void main(String[] args) {
		ATMDepartment department = new ATMDepartment("Department 1");
		
		ATM atm1 = new ATM(1, 5, Denomination.ONE_THOUSAND, Denomination.FIVE_HUNDRED, Denomination.ONE_HUNDRED);
		department.add(atm1);
				
		ATM atm2 = new ATM(2,  15);
		department.add(atm2);
				
		ATM atm3 = new ATM(3, 10, Denomination.FIVE_THOUSAND, Denomination.ONE_THOUSAND);
		department.add(atm3);
		department.print();
				
		ATMDepartment mainDepartment = new ATMDepartment("Main Department");

		ATM atm4 = new ATM(4);
		mainDepartment.add(atm4);
		mainDepartment.add(department);
		mainDepartment.print();
		
		try {
			System.out.println("Dispense 5000");
			atm1.dispense(5000);
			
			System.out.println("Dispense 10000");
			atm2.dispense(10000);
			
			System.out.println("Dispense 10000");
			atm3.dispense(10000);
			
			System.out.println("Dispense 10000");
			atm4.dispense(10000);
			
			System.out.println("Accept 5000");
			atm2.accept(Denomination.ONE_THOUSAND, 5);
			
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
		
		mainDepartment.print();

		System.out.println("Restore state");
		mainDepartment.restore();
		
		mainDepartment.print();
	}
}
