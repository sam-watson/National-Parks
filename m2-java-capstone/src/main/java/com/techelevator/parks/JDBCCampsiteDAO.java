package com.techelevator.parks;

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
			Campsite site = new Campsite();
			site.setSiteId(row.getLong("site_id"));
			site.setCampgroundId(campgroundId);
			site.setAccessible(row.getBoolean("accessible"));
			site.setHasUtilities(row.getBoolean("utilities"));
			site.setMaxOccupancy(row.getInt("max_occupancy"));
			site.setMaxRVLength(row.getInt("max_rv_length"));
			site.setSiteNumber(row.getString("site_number"));
			sites.add(site);
		}
		return sites ;
	}
	
	public List<Campsite> getAvailableCampsitesByDate(LocalDate start, LocalDate end) {
		List<Campsite> availableSites = new ArrayList<>();
		String sqlSelectSitesByDate = "SELECT * FROM site s OUTER JOIN reservation r ON s.site_id = r.site_id WHERE "
				+ "(SELECT (DATE from_date, DATE to_date) OVERLAPS (DATE ?, DATE ?) = FALSE"; //probably won't work work, wrong data types
		template.queryForRowSet(sqlSelectSitesByDate);
		return availableSites;
	}
}
