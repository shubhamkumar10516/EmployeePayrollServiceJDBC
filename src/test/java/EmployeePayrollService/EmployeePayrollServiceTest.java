package EmployeePayrollService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import EmployeePayrollService.EmployeePayrollService.IOService;


public class EmployeePayrollServiceTest {
	// Testing entry count
	@Test
	public void  givenEmployeeReturnEntryCount() {
		EmployeePayrollData[] arrOfEmp = {
				new EmployeePayrollData(1, "Jeff", 100.0),
                new EmployeePayrollData(2, "Bill", 200.0),
                new EmployeePayrollData(3, "Mark", 300.0)
		};
		EmployeePayrollService empService;
		empService = new EmployeePayrollService(Arrays.asList(arrOfEmp));
        empService.writeEmployeePayrollData(IOService.FILE_IO);
        empService.printData(IOService.FILE_IO);   
        long entries = empService.countEntries(IOService.FILE_IO);
        System.out.println(entries);
        assertEquals(3, entries);
	}

	@Test
	public void fileEntriesCountTest() {
		EmployeePayrollService empService = new EmployeePayrollService();
		long count = empService.countEntries(IOService.FILE_IO);
		assertEquals(3, count);
	}
	
	@Test
	public void readEmployeePayrollDataTest() {
		EmployeePayrollService empService = new EmployeePayrollService();
		long count = empService.readEmployeePayrollData(IOService.FILE_IO);
		assertEquals(3, count);
	}
	
	@Test
	public void returnCountOfEmployeeFromDB() {
		EmployeePayrollService empService = new EmployeePayrollService();
	    List<EmployeePayrollData> employeePayrollData = empService.readEmployeePayrollDatas(IOService.DB_IO);
	    assertEquals(3, employeePayrollData.size());
	}
	
	@Test
	public void updatesalaryAndSynWithDatabase() {
		EmployeePayrollService empService = new EmployeePayrollService();
	 //   List<EmployeePayrollData> employeePayrollData = empService.readEmployeePayrollDatas(IOService.DB_IO);
	    int result = empService.updateSalary("ram", 300000.00);
	    //assertTrue(1 == result);
	    boolean result1 = empService.checkEmployeePayrollSynWithDB("ram");
	    assertTrue(result == 1);
	}
	
	@Test
	public void givenStartAndEndDateReturnEmployeeJoinedBetweenDates() throws EmployeeCustomException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollDataByDate(IOService.DB_IO, "2016-01-01","2020-01-01");
		System.out.println(employeePayrollData);
		assertEquals(3, employeePayrollData.size());
	}
	
	@Test
	public void givenEmployeeDataInDBReturnCountOfEmployee() throws EmployeeCustomException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Integer> map = employeePayrollService.getCountByGender(IOService.DB_IO);
		assertEquals((int) map.get("m"), 2);
		assertEquals((int) map.get("f"), 1);
	}
	
	@Test
	public void givenEmployeeDataInDBReturnEmployeeByGenderByMinSalary() throws EmployeeCustomException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Integer> map = employeePayrollService.getLeastSalaryByGender();
		assertEquals((int) map.get("m"), 1000000);
		assertEquals((int) map.get("f"), 3000000);
	}
	
	@Test
	public void givenEmployeeDataInDBReturnEmployeeByGenderByAverageSalary() throws EmployeeCustomException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Integer> map = employeePayrollService.getAverageSalaryByGender();
		assertEquals((int) map.get("m"), 1000000);
		assertEquals((int) map.get("f"), 3000000);
	}
	
	@Test
	public void givenNewAddedShouldBeInSyncWithDB() throws EmployeeCustomException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.addEmployeeToPayroll(4,"Mark",5000000,LocalDate.now(),"M");
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
		assertTrue(result);
	}
	@Test
	public void givenEmployeeDeletedBeRemovedFromEmployeeList() throws EmployeeCustomException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService .readEmployeePayrollData(IOService.DB_IO);
		List<EmployeePayrollData> list = employeePayrollService.deleteEmployee("Mark",false);
		System.out.println("size.."+list.size());
		assertEquals(4, list.size());
	}
	
	@Test
	public void given6EmployeeAddedToDbShouldMatch() {
		
		EmployeePayrollData[] arrOfemp = {new EmployeePayrollData(0, "rajeshwar", 150000.0, LocalDate.now(), "m"),
				new EmployeePayrollData(0, "shami", 250000.0, LocalDate.now(), "m"),
				new EmployeePayrollData(0, "somu", 150000.0, LocalDate.now(), "f"),
				new EmployeePayrollData(0, "ashu", 450000.0, LocalDate.now(), "m"),
				new EmployeePayrollData(0, "naresh", 150000.0, LocalDate.now(),"m"),
				new EmployeePayrollData(0, "rawar", 150000.0, LocalDate.now(), "m")};
		
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollDatas(IOService.DB_IO);
		Instant start = Instant.now();
		employeePayrollService.addEmployeeToPayroll(Arrays.asList(arrOfemp));
		Instant end = Instant.now();
		System.out.println("Duration taken without threads: "+ Duration.between(start, end));
		Instant start1 = Instant.now();
		employeePayrollService.addEmployeeToPayrollUsingThreads(Arrays.asList(arrOfemp));
		Instant end1 = Instant.now();
		System.out.println("Duration taken with threads: "+ Duration.between(start, end));
		assertEquals(15, employeePayrollService.countEntries(IOService.DB_IO));
	}
}
