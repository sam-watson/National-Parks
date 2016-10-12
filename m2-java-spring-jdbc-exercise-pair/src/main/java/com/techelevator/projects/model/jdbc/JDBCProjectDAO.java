package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		List<Project> projects = new ArrayList<>();
		String sqlSelectAllActiveProjects = "SELECT * FROM project ";
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlSelectAllActiveProjects);
		while(rowSet.next()) {
			Project proj = new Project();
			proj.setName(rowSet.getString("name"));
			proj.setId(rowSet.getLong("project_id"));
			if (rowSet.getDate("from_date") != null) {
				proj.setStartDate(rowSet.getDate("from_date").toLocalDate());
				if (proj.getStartDate().isAfter(LocalDate.now())) {
					continue;
				}
			} else continue;
			if (rowSet.getDate("to_date") != null) {
				proj.setEndDate(rowSet.getDate("to_date").toLocalDate());
				if (proj.getEndDate().isAfter(LocalDate.now())) {
					projects.add(proj);
				}
			} else {
				projects.add(proj);
			}
		}
		return projects;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String sqlDeleteEmployeeFromProject = "DELETE FROM project_employee WHERE employee_id=? AND project_id=?";
		jdbcTemplate.update(sqlDeleteEmployeeFromProject,employeeId, projectId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String sqlInsertNewEmployeeToProject = "INSERT INTO project_employee (project_id, employee_id) VALUES (?, ?)";
		jdbcTemplate.update(sqlInsertNewEmployeeToProject, projectId, employeeId);
	}
	
	public void createNewProject(Project newProject) {
		String sqlInsertNewProject = "INSERT INTO project (project_id, name) VALUES (?, ?)";
		jdbcTemplate.update(sqlInsertNewProject, newProject.getId(), newProject.getName());
	}
}
