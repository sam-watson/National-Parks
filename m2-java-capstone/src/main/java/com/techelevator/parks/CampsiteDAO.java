package com.techelevator.parks;

import java.util.List;

public interface CampsiteDAO {
	
	//public List<Campsite> getAllCampsites();

	public List<Campsite> getAllCampsitesForCampground(Long campgroundId);
	
}
