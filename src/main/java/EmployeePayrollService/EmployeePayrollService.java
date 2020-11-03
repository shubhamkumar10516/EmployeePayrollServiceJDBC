package EmployeePayrollService;

import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollService {
	enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO
	}

	// list of employee
	private List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDBService employeePayrollDBService;
    
    
	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this();
		this.employeePayrollList = employeePayrollList;
	}

	
	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	// takes input from console
	private void readEmployeePayrollData(Scanner consoleInputReader) {
		System.out.println("Enter emp Id: ");
		int id = consoleInputReader.nextInt();
		consoleInputReader.nextLine();
		System.out.println("Enter employee name: ");
		String name = consoleInputReader.nextLine();
		System.out.println("Enter employee salary: ");
		double salary = consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id, name, salary));
	}

	// read from database
	public List<EmployeePayrollData> readEmployeePayrollDatas(IOService iService) {
		this.employeePayrollList = employeePayrollDBService.readData();
		return employeePayrollList;
	}

	// update database
	public int updateSalary(String name, double salary) {
		int result = employeePayrollDBService.updateEmpSalary(name, salary);
		if (result == 0)
			return 0;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.salary = salary;
		return result;
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream().filter(employeeData -> employeeData.name.equals(name)).findFirst()
				.orElse(null);
	}

	public boolean checkEmployeePayrollSynWithDB(String name) {
		List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

	// output on the console
	public void writeEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.CONSOLE_IO)) {
			System.out.println("employee details :: ");
			for (EmployeePayrollData e : employeePayrollList) {
				e.display();
			}
		} else if (ioService.equals(IOService.FILE_IO)) {
			new EmployeePayrolFileIOService().writeData(employeePayrollList);
		}
	}

	// print data according io services
	public void printData(IOService ioServices) {
		if (ioServices.equals(IOService.FILE_IO))
			new EmployeePayrolFileIOService().printData();
	}

	// counting the entries
	public long countEntries(IOService fileIo) {
		if (fileIo.equals(IOService.FILE_IO))
			return new EmployeePayrolFileIOService().countEntries();
		return 0;
	}

	public long readEmployeePayrollData(IOService ioService) {
		if (ioService.equals(IOService.FILE_IO)) {
			this.employeePayrollList = new EmployeePayrolFileIOService().readData();
		}
		return this.employeePayrollList.size();
	}


	public List<EmployeePayrollData> readEmployeePayrollDataByDate(IOService dbIo, String start, String end) {
		if(dbIo.equals(IOService.DB_IO))
			this.employeePayrollList = employeePayrollDBService.getDataWithinDates(start, end);
		return this.employeePayrollList;
	}


	public Map<String, Integer> getCountByGender(IOService dbIo) {
		Map<String, Integer> employeeCountByGenderMap = employeePayrollDBService.getCountByGender();
		return  employeeCountByGenderMap;
	}


	public Map<String, Integer> getLeastSalaryByGender() {
		Map<String, Integer> employeeLeastSalaryMap = employeePayrollDBService.getLeastSalaryByGender();
		return employeeLeastSalaryMap;
	}


	public Map<String, Integer> getAverageSalaryByGender() {
		Map<String, Integer> employeeAverageSalaryMap = employeePayrollDBService.getAverageSalaryByGender();
		return employeeAverageSalaryMap;
	}


	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}


	public void addEmployeeToPayroll(String name, String gender, int salary, LocalDate date) {
		employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name,gender,salary,date));
		
	}


	public List<EmployeePayrollData> deleteEmployee(String name, boolean isActive) {
		int update = employeePayrollDBService.deleteEmployee(name, isActive);
		if(update == 1) {
			Iterator<EmployeePayrollData> itr = employeePayrollList.iterator();
			while(itr.hasNext()) {
				EmployeePayrollData employee = itr.next();
				if(employee.name.equals(name)) {
					itr.remove();
				}
			}
		}
		return employeePayrollList;
	}
	}

