package com.techelevator.parks;

public class Campsite {

	private Long siteId;
	private Long campgroundId;
	private String siteNumber;
	private int maxOccupancy;
	private boolean isAccessible;
	private int maxRVLength;
	private boolean hasUtilities;
	
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public Long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	public String getSiteNumber() {
		return siteNumber;
	}
	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public boolean isAccessible() {
		return isAccessible;
	}
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	public int getMaxRVLength() {
		return maxRVLength;
	}
	public void setMaxRVLength(int maxRVLength) {
		this.maxRVLength = maxRVLength;
	}
	public boolean isHasUtilities() {
		return hasUtilities;
	}
	public void setHasUtilities(boolean hasUtilities) {
		this.hasUtilities = hasUtilities;
	}
	
	public String toString() {
		return siteNumber +"\t" + maxOccupancy + "\t" + isAccessible + "\t" + maxRVLength + "\t" + hasUtilities;
	}
}
