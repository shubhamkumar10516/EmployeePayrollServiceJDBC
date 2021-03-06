package EmployeePayrollService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmployeePayrollDBService {
	private int connectionCounter = 0;
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;
	HashMap<String, Integer> operationMap;
	private List<EmployeePayrollData> employeePayrollList;

	private EmployeePayrollDBService() {

	}

	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}

	private Connection getConnection() {
		connectionCounter++;
		String jdbcURL = "jdbc:mysql://localhost:3306/new_payroll_service?useSSL=false";
		String user = "root";
		String password = "password";
		Connection connection = null;
		try {
			System.out.println("connecting to " + Thread.currentThread().getName() + " id : " + connectionCounter);
			connection = DriverManager.getConnection(jdbcURL, user, password);
			System.out.println("connected to " + Thread.currentThread().getName() + " id : " + connectionCounter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public List<EmployeePayrollData> readData() {
		String sql = "SELECT * from employee_payroll;";
		employeePayrollList = new ArrayList<>();
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

	public int updateEmpSalary(String name, double salary) {
		return this.updateSalaryUsingStatement(name, salary);
	}

	private int updateSalaryUsingStatement(String name, double salary) {
		String sql = String.format("update employee_payroll set salary = %.2f where name = '%s'", salary, name);
		try (Connection connection = getConnection();) {
			Statement statement = connection.createStatement();
			System.out.println(statement.executeUpdate(sql));
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		employeePayrollList = new ArrayList<>();
		if (this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet result) {

		employeePayrollList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private void prepareStatementForEmployeeData() {
		Connection connection = this.getConnection();
		String sql = "select * from employee_payroll where name = ?";
		try {
			employeePayrollDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<EmployeePayrollData> getDataWithinDates(String start, String end) {
		String sql = String.format(
				"SELECT * FROM employee_payroll WHERE start BETWEEN CAST('%s' AS DATE) AND CAST('%s' AS DATE)", start,
				end);
		return getDataFromDatabaseBySQL(sql);
	}

	private List<EmployeePayrollData> getDataFromDatabaseBySQL(String sql) {
		employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate start = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, start));
			}
			return employeePayrollList;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Integer> getCountByGender() {
		String sql = "SELECT gender, COUNT(name) from employee_payroll GROUP BY gender;";
		operationMap = new HashMap<String, Integer>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String gender = resultSet.getString("gender");
				int count = resultSet.getInt("COUNT(name)");
				operationMap.put(gender, count);
			}
			return operationMap;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Integer> getLeastSalaryByGender() {
		String sql = "SELECT gender, MIN(salary) from employee_payroll GROUP BY gender;";
		operationMap = new HashMap<String, Integer>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String gender = resultSet.getString("gender");
				int count = resultSet.getInt("MIN(salary)");
				operationMap.put(gender, count);
			}
			return operationMap;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, Integer> getAverageSalaryByGender() {
		String sql = "SELECT gender, AVG(salary) from employee_payroll GROUP BY gender;";
		operationMap = new HashMap<String, Integer>();
		try (Connection connection = this.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String gender = resultSet.getString("gender");
				int count = resultSet.getInt("AVG(salary)");
				operationMap.put(gender, count);
			}
			return operationMap;
		} catch (SQLException e) {
			System.out.println("Exception occurred..." + e);
		}
		return null;
	}

	public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate date, String gender) {
		int employeeId = -1;
		EmployeePayrollData employeePayrollData = null;
		String sql = String.format(
				"INSERT INTO employee_payroll(name,gender,salary,start) VALUES ('%s','%s','%s','%s')", name, gender,
				salary, Date.valueOf(date));
		try (Connection connection = this.getConnection()) {
			java.sql.Statement statement = connection.createStatement();
			System.out.println("******"+statement);
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			System.out.println("****"+ rowAffected);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(1);
			}
			employeePayrollData = new EmployeePayrollData(employeeId, name, salary, date, gender);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollData;
	}

	public int deleteEmployee(String name, boolean isActive) {
		String sql = String.format("update employee_payroll set is_active =  %s where name = '%s';", isActive, name);
		try (Connection connection = this.getConnection()) {
			java.sql.Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
