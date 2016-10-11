package com.techelevator.projects.view;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;

public class JDBCEmployeeDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCEmployeeDAO dao;
	private JdbcTemplate jdbcTemplate;

	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections 
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setup() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		dao = new JDBCEmployeeDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void dao_returns_correct_number_of_employees() {
		jdbcTemplate.update("DELETE FROM project_employee");
		String sqlDeleteAllEmployees = "DELETE FROM employee";
		jdbcTemplate.update("DELETE FROM employee");
		Random rand = new Random();
		int numberOfEmployees = 4;
		String sqlInsertNewEmployee = "INSERT INTO employee VALUES ?,?,?,?,?,?,?";
		for (int i = 0; i < numberOfEmployees; i++) {
			jdbcTemplate.update(sqlDeleteAllEmployees, rand.nextLong(), rand.nextLong(), Integer.toString(rand.nextInt()), Integer.toString(rand.nextInt()), LocalDate.now(), 'F', LocalDate.now());
		}
		List<Employee> employees = dao.getAllEmployees();
		assertEquals(numberOfEmployees, employees.size());
	}
	
	private Employee getDummyEmployee() {
		Random rand = new Random();
		return getNewEmployee(rand.nextLong(), rand.nextLong(), Integer.toString(rand.nextInt()), Integer.toString(rand.nextInt()), LocalDate.now(), 'F', LocalDate.now());
	}
	
	private Employee getNewEmployee(Long employee_id, Long departmentId, String firstName, String lastName, LocalDate birthDay, char gender, LocalDate hireDate) {
		Employee employee = new Employee();
		employee.setId(employee_id);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setBirthDay(birthDay);
		employee.setDepartmentId(departmentId);
		employee.setGender(gender);
		employee.setHireDate(hireDate);
		return employee;
	}
}
