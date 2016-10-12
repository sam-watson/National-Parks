package com.techelevator.projects.view;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.jdbc.JDBCEmployeeDAO;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JDBCEmployeeDAOTest {
	
	JDBCEmployeeDAO dao;
	protected static SingleConnectionDataSource dataSource;
	protected JdbcTemplate jdbcTemplate;

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
		clearAllEmployees();
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void dao_returns_correct_number_of_employees() {
		int numberOfEmployees = 4;
		for (int i = 0; i < numberOfEmployees; i++) {
			//SqlRowSet = jdbcTemplate.queryForRowSet("SELECT nextval('seq_employee_id')");
			addEmployeeToDB();
		}
		List<Employee> employees = dao.getAllEmployees();
		assertEquals(numberOfEmployees, employees.size());
	}
	
	@Test
	public void dao_returns_correct_employee_by_name() {
		addEmployeeToDB("Vineeta","Mandava");
		addEmployeeToDB("Sam","Watson");
		addEmployeeToDB("Leon","Melnick");
		List<Employee> employee = dao.searchEmployeesByName("Vineeta","Mandava");
		for(Employee emp: employee) {
			assertEquals("Vineeta",emp.getFirstName());
		}
		
	}
	
	@Test
	public void dao_returns_correct_employees_with_same_first_name() {
		addEmployeeToDB("Vineeta","Mandava");
		addEmployeeToDB("Sam","Watson");
		addEmployeeToDB("Sam","Jackson");
		addEmployeeToDB("Leon","Melnick");
		List<Employee> employee = dao.searchEmployeesByName("Sam","");
		for(Employee emp: employee) {
			assertEquals("Sam",emp.getFirstName());
			}
		assertEquals(2, employee.size());
		
	}
	
	@Test
	private void dao_returns_correct_employee_by_project_id() {
		addEmployeeToDB("Vineeta","Mandava");
		addEmployeeToDB("Sam","Watson");
		addEmployeeToDB("Sam","Jackson");
		addEmployeeToDB("Leon","Melnick");
		JDBCProjectDAO pdao = new JDBCProjectDAO(dataSource);
		//pdao.addEmployeeToProject(new Long(1),new Long(1));
	}
	
	private void addEmployeeToDB() {
		addEmployeeToDB("John","Smith");
	}
	
	private void addEmployeeToDB(String firstName, String lastName) {
		Random rand = new Random();
		String sqlInsertNewEmployee = "INSERT INTO employee (department_id, first_name, last_name, birth_date, gender, hire_date) VALUES (?,?,?,?,?,?)";
		jdbcTemplate.update(sqlInsertNewEmployee, 1, firstName, lastName, LocalDate.now(), 'F', LocalDate.now());
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
	
	private void clearAllEmployees() {
		jdbcTemplate.update("DELETE FROM project_employee");
		jdbcTemplate.update("DELETE FROM employee");
	}
}
