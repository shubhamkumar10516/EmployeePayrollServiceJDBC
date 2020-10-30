package EmployeePayrollService;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import org.junit.Test;


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
}
