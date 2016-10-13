package com.techelevator;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.techelevator.parks.Campground;
import com.techelevator.parks.CampgroundDAO;
import com.techelevator.parks.JDBCCampgroundDAO;
import com.techelevator.parks.JDBCParkDAO;
import com.techelevator.parks.Park;
import com.techelevator.parks.ParkDAO;

public class JDBCCampgroundDAOTest extends DAOIntegrationTest {
	CampgroundDAO dao;

	@Before
	public void setupDAO() {
		DataSource dataSource = getDataSource();
		dao = new JDBCCampgroundDAO(dataSource);
	}
	@Test
	public void dao_returns_all_campgrounds_for_a_park() {
		addParkToDatabase(1);
		addCampgroundToDatabase(1l);
		addCampgroundToDatabase(1l);
		Park park = new Park();
		park.setId(1l);
		List<Campground> camps = dao.getAllCampgrounds(park);
		assertEquals(2, camps.size());
	}
	
	

}
