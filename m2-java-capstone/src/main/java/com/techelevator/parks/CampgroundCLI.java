package com.techelevator.parks;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class CampgroundCLI {
	private static final String MAIN_MENU_OPTION_PARKS = "Select A Park";
	
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS };

	private static final String PARK_MENU_OPTION_ALL_CAMPGROUNDS = "List All Campgrounds";
	
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_ALL_CAMPGROUNDS };

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		// instantiate your DAOs here
		// What DAOs to make
		this.menu = new Menu(System.in, System.out);
		this.parkDAO = new JDBCParkDAO(datasource);
		this.campDAO = new JDBCCampgroundDAO(datasource);
	}

	public void run() {
		while (true) {
			printHeading("Select A Park");
			handleListAndSelectParks();		}
	}

	private void handleListAndSelectParks() {
		List<Park> parks = parkDAO.getAllParks();
		Park choice = (Park)menu.getChoiceFromOptions(parks.toArray());
		displayParkInfo(choice);
		handleParkOption(choice);
	}

	private void displayParkInfo(Park park) {
		printHeading("Park Information");
		System.out.println(park.getName()+" National Park");
		System.out.println("Location: " + park.getLocation());
		System.out.println("Established: " + park.getEstDate());
		System.out.println("Area: "+ park.getArea());
		System.out.println("Annual Visitors: " + park.getVisitors());
		System.out.println();
		System.out.println(park.getDesc());
	}
	
	private void handleParkOption(Park park) {
		String choice = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		if (choice.equals(PARK_MENU_OPTION_ALL_CAMPGROUNDS)) {
			handleListAllCampgrounds(park);
		}
	}
	
	private void handleListAllCampgrounds(Park park) {
		List<Campground> campgrounds = campDAO.getAllCampgrounds(park);
		Campground choice = (Campground)menu.getChoiceFromOptions(campgrounds.toArray());
	}

	private void printHeading(String headingText) {
		System.out.println("\n\b" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
