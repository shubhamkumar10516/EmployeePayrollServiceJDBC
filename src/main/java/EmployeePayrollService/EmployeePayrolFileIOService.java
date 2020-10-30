package EmployeePayrollService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class EmployeePayrolFileIOService {
	public static String PAYROLL_FILE_NAME = "payroll-file.txt";

	// Writing list of data to file
	public void writeData(List<EmployeePayrollData> employeePayrollList) {
		StringBuffer empBuffer = new StringBuffer();
		employeePayrollList.forEach(employee -> {
			String employeeDataString = employee.display().concat("\n");
			empBuffer.append(employeeDataString);
		});
		try {
			Files.write(Paths.get(PAYROLL_FILE_NAME), empBuffer.toString().getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// printing the lines of file
	public void printData() {
		try {
			Files.lines(new File(PAYROLL_FILE_NAME).toPath()).forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// counting the entries of file
	public long countEntries() {
		int entries = 0;
		try {
			entries = (int) Files.lines(new File(PAYROLL_FILE_NAME).toPath()).count();
		} catch (IOException e) {
			return entries;
		}
		return entries;
	}

	// read data
	public List<EmployeePayrollData> readData() {
		List<EmployeePayrollData> empList = new ArrayList<EmployeePayrollData>();
		try {
			Files.lines(new File(PAYROLL_FILE_NAME).toPath()).map(line -> line.trim()).forEach(line -> {
				String[] str = line.split(":");
				int id = Integer.parseInt(str[0]);
				String name = str[1];
				double salary = Double.parseDouble(str[2]);
				empList.add(new EmployeePayrollData(id, name, salary));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return empList;
	}

}