package EmployeePayrollService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

	private Connection getConnection() {
		String jdbcURL = "jdbc:mysql://localhost:3306/new_payroll_service?useSSL=false";
		String user = "root";
		String password = "password";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(jdbcURL, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public List<EmployeePayrollData> readData() {
		String sql = "SELECT * from employee_payroll;";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		Connection connection = this.getConnection();
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

}
