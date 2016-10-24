package com.techelevator.parks;

import java.time.LocalDate;
import java.util.List;

public interface CampsiteDAO {
	
	//public List<Campsite> getAllCampsites();

	public List<Campsite> getAllCampsitesForCampground(Long campgroundId);
	public List<Campsite> getAvailableCampsitesByDate(LocalDate start, LocalDate end, Long campgroundId);
	
}
