package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.techelevator.parks.JDBCParkDAO;
import com.techelevator.parks.Park;
import com.techelevator.parks.ParkDAO;

public class JDBCParkDAOTest extends DAOIntegrationTest {

	ParkDAO dao;

	@Before
	public void setupDAO() {
		DataSource dataSource = getDataSource();
		dao = new JDBCParkDAO(dataSource);
		
	}

	@Test
	public void dao_returns_all_parks() {
		addParksToDatabase(5);
		List<Park> parks = dao.getAllParks();
		assertEquals(5, parks.size());
	}

	@Test
	public void testReturnParkNames() {
		addParkToDatabase("Vineeta Park");
		Park park = dao.getAllParks().get(0);
		assertEquals("Vineeta Park", park.getName());
	}

	@Test
	public void returnsAllParkNamesInAscOrder() {
		addParkToDatabase("Vineeta- Honest Fun Park");
		addParkToDatabase("Leon-The Lion Park");
		addParkToDatabase("Sam- SamTheMan Park");
		List<Park> parkName = dao.getAllParks();
		assertEquals("Leon-The Lion Park", parkName.get(0).getName());
		assertEquals("Sam- SamTheMan Park", parkName.get(1).getName());
		assertEquals("Vineeta- Honest Fun Park", parkName.get(2).getName());
	}
	
	@Test
	public void can_retrieve_park_info() {
		addParkToDatabase();
		Park park = dao.getAllParks().get(0);
		assertEquals("Cleveland",park.getLocation());
	}
}
