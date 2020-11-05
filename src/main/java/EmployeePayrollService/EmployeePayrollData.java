package EmployeePayrollService;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {

	public int id;
	public String name;
	public double salary;
	public LocalDate startDate;
	public String gender;
	public EmployeePayrollData(int id, String name, double salary) {
		this.id = id;
		this.name = name;
		this.salary = salary;
	}
	public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
        this(id, name, salary);
		this.startDate = startDate;
	}
	public EmployeePayrollData(int id, String name, double salary, LocalDate startDate, String gender) {
		this(id, name, salary, startDate);
		this.gender = gender;
	}
	public String toString() {
	     return (id+":"+ name+ ":"+ salary+" : "+startDate);
	}
	public String display() {
	     return (id+":"+ name+ ":"+ salary);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, gender, salary, startDate);
	}
}
