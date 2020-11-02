package EmployeePayrollService;

import java.time.LocalDate;

public class EmployeePayrollData {

	private int id;
	public String name;
	public double salary;
	private LocalDate startDate; 
	public EmployeePayrollData(int id, String name, double salary) {
		this.id = id;
		this.name = name;
		this.salary = salary;
	}
	public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
        this(id, name, salary);
		this.startDate = startDate;
	}
	public String toString() {
	     return (id+":"+ name+ ":"+ salary+" : "+startDate);
	}
	public String display() {
	     return (id+":"+ name+ ":"+ salary);
	}
}
