package com.techelevator.parks;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampsiteDAO implements CampsiteDAO {
	
	private JdbcTemplate template;
	
	public JDBCCampsiteDAO(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campsite> getAllCampsitesForCampground(Long campgroundId) {
		List<Campsite> sites = new ArrayList<>();
		String sqlSelectAllCampsitesForCampground = "SELECT * FROM site s JOIN campground c ON s.campground_id = c.campground_id WHERE s.campground_id = ?";
		SqlRowSet row = template.queryForRowSet(sqlSelectAllCampsitesForCampground,campgroundId);
		while(row.next()) {
			Campsite site = createCampsite(row);
			sites.add(site);

		}
		return sites ;
	}

	private Campsite createCampsite(SqlRowSet row) {
		Campsite site = new Campsite();
		site.setSiteId(row.getLong("site_id"));
		site.setCampgroundId(row.getLong("campground_id"));
		site.setAccessible(row.getBoolean("accessible"));
		site.setHasUtilities(row.getBoolean("utilities"));
		site.setMaxOccupancy(row.getInt("max_occupancy"));
		site.setMaxRVLength(row.getInt("max_rv_length"));
		site.setSiteNumber(row.getString("site_number"));
		return site;
	}
	
	public List<Campsite> getAvailableCampsitesByDate(LocalDate start, LocalDate end) {
		List<Campsite> availableSites = new ArrayList<>();
		String sqlSelectSitesByDate = "SELECT s.site_id, campground_id, s.site_number, max_occupancy, accessible, max_rv_length, utilities "
				+ "FROM site s FULL OUTER JOIN reservation r ON s.site_id = r.site_id "
				+ "WHERE ( from_date, to_date) OVERLAPS (?, ?) = false "
				+ "GROUP BY s.site_id, campground_id, s.site_number, max_occupancy, accessible, max_rv_length, utilities  ORDER BY s.site_number";
				
//				"SELECT * FROM site s FULL OUTER JOIN reservation r ON s.site_id = r.site_id WHERE "
//				+ " ( from_date, to_date) OVERLAPS ( ?,  ?) = false";
		
		SqlRowSet rows = template.queryForRowSet(sqlSelectSitesByDate, start, end);
		while (rows.next()) {
			Campsite site = createCampsite(rows);
			availableSites.add(site);
		}
		
		return availableSites;
	}
}
