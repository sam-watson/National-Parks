package com.techelevator;

import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public abstract class DAOIntegrationTest {

	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private JdbcTemplate template;

	
	/* Before any tests are run, this method initializes the datasource for testing. */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections 
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
	}
	
	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setUp() {
		template=new JdbcTemplate(dataSource);
        clearDatabase();
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	/* This method provides access to the DataSource for subclasses so that 
	 * they can instantiate a DAO for testing */
	protected DataSource getDataSource() {
		return dataSource;
	}
	
	protected JdbcTemplate getTemplate() {
		return template;
	}
	
	protected void clearDatabase() {
		template.update("DELETE FROM reservation");
		template.update("DELETE FROM site");
		template.update("DELETE FROM campground");
		template.update("DELETE FROM park");
	}
	
	protected void addParksToDatabase(int numberOfParks) {
		for (int i = 0; i < numberOfParks; i++) {
			String sqlInsertPark = "INSERT INTO park (name, location, establish_date, area, visitors, description) VALUES (?,?,?,?,?,?)";
			getTemplate().update(sqlInsertPark, "Park" + i, "Cleveland", LocalDate.now(), Math.random(), Math.random(),
					"A nice park.");
		}
	}

	protected void addParkToDatabase() {
		addParkToDatabase("Generic park");
	}

	protected void addParkToDatabase(String name) {
		String sqlInsertPark = "INSERT INTO park (name, location, establish_date, area, visitors, description) VALUES (?,?,?,?,?,?)";
		getTemplate().update(sqlInsertPark, name, "Cleveland", LocalDate.now(), Math.random(), Math.random(),
				"A nice park.");
	}
	
	protected void addParkToDatabase(int id) {
		String sqlInsertPark = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description) VALUES (?,?,?,?,?,?,?)";
		getTemplate().update(sqlInsertPark, id, "park", "Cleveland", LocalDate.now(), Math.random(), Math.random(),
				"A nice park.");
	}
	
	protected void addCampgroundToDatabase(Long parkId) {
		String sqlInsertCampground = "INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (?,?,?,?,?)";
		getTemplate().update(sqlInsertCampground, parkId, "Campground", 01, 01, 1);
	}
	
}
