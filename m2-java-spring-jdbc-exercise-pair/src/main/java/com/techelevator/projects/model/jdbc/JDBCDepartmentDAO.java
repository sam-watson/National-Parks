package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> results = new ArrayList<>();
		
		String sqlSelectAllDepartments = "SELECT * FROM department";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlSelectAllDepartments);
		while(rows.next()) {
			Department dept = new Department();
			dept.setId(rows.getLong("department_id"));
			dept.setName(rows.getString("name"));
			results.add(dept);
		}
		return results;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List<Department> results = new ArrayList<>();
		String sqlSelectDepartmentsByName = "SELECT department_id, name FROM department WHERE name LIKE ?";
		//convert from string to sql query, LIKE %%
		String queryParam = "%" + nameSearch + "%";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlSelectDepartmentsByName, queryParam);
		//get query results
		//create objects from results
		while(rows.next()) {
			Department dept = new Department();
			dept.setId(rows.getLong(1));
			dept.setName(rows.getString(2));
			results.add(dept);
		}

		return results;
	}

	@Override
	public void updateDepartmentName(Long departmentId, String departmentName) {
		
	}

	@Override
	public Department createDepartment(String departmentName) {
		
		return null;
	}

}
