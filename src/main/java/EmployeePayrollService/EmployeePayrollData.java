package EmployeePayrollService;

public class EmployeePayrollData {

	private int id;
	private String name;
	private double salary;
	public EmployeePayrollData(int id, String name, double salary) {
		this.id = id;
		this.name = name;
		this.salary = salary;
	}
	public String display() {
	     return (id+":"+ name+ ":"+ salary);
	}
}
