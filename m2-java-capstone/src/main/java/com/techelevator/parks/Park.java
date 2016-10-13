package com.techelevator.parks;

import java.time.LocalDate;

public class Park {

	private Long id;
	private String name;
	private String location;
	private LocalDate estDate;
	private int area;
	private int visitors;
	private String desc;
	
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public LocalDate getEstDate() {
		return estDate;
	}
	public void setEstDate(LocalDate estDate) {
		this.estDate = estDate;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getVisitors() {
		return visitors;
	}
	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String toString() {
		return name;
	}
	
}
