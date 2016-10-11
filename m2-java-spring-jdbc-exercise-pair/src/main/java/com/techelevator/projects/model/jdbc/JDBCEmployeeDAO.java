package com.techelevator.projects.model.jdbc;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.time.*;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employeeList = new ArrayList<>();
		String sqlSelectAllEmployees = "SELECT * FROM employee";
		SqlRowSet rowSet=jdbcTemplate.queryForRowSet(sqlSelectAllEmployees);
		while(rowSet.next()) {
			Employee employee = new Employee();
			employee.setId(rowSet.getLong("employee_id"));
			employee.setFirstName(rowSet.getString("first_name"));
			employee.setLastName(rowSet.getString("last_name"));
			employee.setBirthDay(rowSet.getDate("birth_date").toLocalDate());
			employee.setDepartmentId(rowSet.getLong("department_id"));
			employee.setGender(rowSet.getString("gender").charAt(0));
			employee.setHireDate(rowSet.getDate("hire_date").toLocalDate());
			employeeList.add(employee);
		}
		return employeeList;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		List<Employee> employeeList= new ArrayList<>();
		String searchEmployeeByName="select last_name,first_name from employee where last_name=? and first_name=?";
		SqlRowSet rowSet=jdbcTemplate.queryForRowSet(searchEmployeeByName,lastNameSearch,firstNameSearch);
		
		while(rowSet.next()){
			Employee employee= new Employee();
			employee.setLastName(rowSet.getString("last_name"));
			employee.setFirstName(rowSet.getString("first_name"));
			employeeList.add(employee);
			
			
			
		}
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		List<Employee> employeeList = new ArrayList<>();
		String sqlEmployeesByDepartmentId = "Select last_name, first_name FROM employee WHERE department_id=?";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlEmployeesByDepartmentId,id);
		
		while (rowSet.next()) {
			Employee employee= new Employee();
			employee.setLastName(rowSet.getString("last_name"));
			employee.setFirstName(rowSet.getString("first_name"));
			employeeList.add(employee);
		}
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		List<Employee> employeeList = new ArrayList<>();
		String sqlEmployeesWithoutProjects = "Select * FROM employee where employee_id NOT IN (select employee_id from project_employee)";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlEmployeesWithoutProjects);	

		while (rowSet.next()) {
			Employee employee= new Employee();
			employee.setLastName(rowSet.getString("last_name"));
			employee.setFirstName(rowSet.getString("first_name"));
			employeeList.add(employee);
		
		}
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		List<Employee> employeeList = new ArrayList<>();
		String sqlEmployeesByProjectId = "SELECT * FROM employee  WHERE employee_id IN(select employee_id from project_employee where project_id=?)";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlEmployeesByProjectId,projectId);
		
		while(rowSet.next()) {
			Employee employee= new Employee();
			employee.setLastName(rowSet.getString("last_name"));
			employee.setFirstName(rowSet.getString("first_name"));
			employeeList.add(employee);
		}
		return employeeList;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		
	}

}
