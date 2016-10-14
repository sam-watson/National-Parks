package com.techelevator.parks;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;

public class Campground {

	private Long id;
	private Long parkId;
	private String name;
	private int openingTime;
	private int closingTime;
	private String dailyFee;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOpeningTime() {
		return openingTime;
	}
	
	public String getOpeningMonth() {
		return new DateFormatSymbols().getMonths()[getOpeningTime()-1];
	}

	public void setOpeningTime(int openingTime) {
		this.openingTime = openingTime;
	}

	public int getClosingTime() {
		return closingTime;
	}
	
	public String getClosingMonth() {
		return new DateFormatSymbols().getMonths()[getClosingTime()-1];
	}

	public void setClosingTime(int closingTime) {
		this.closingTime = closingTime;
	}

	public String getDailyFee() {
		return NumberFormat.getCurrencyInstance().format(Float.parseFloat(dailyFee));
	}

	public void setDailyFee(String dailyFee) {
		this.dailyFee = dailyFee;
	}

	public Long getParkId() {
		return parkId;
	}

	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}
	public String toString() {
		return name;
	}
}
