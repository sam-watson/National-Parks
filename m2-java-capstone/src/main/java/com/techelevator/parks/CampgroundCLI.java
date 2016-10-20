package com.techelevator.parks;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class CampgroundCLI {
	
	private static final String MAIN_MENU_OPTION_PARKS = "Select A Park";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS };
	//park view
	private static final String PARK_MENU_OPTION_ALL_CAMPGROUNDS = "List All Campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH_WHOLE_PARK_SITES = "Search entire park for campsites";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_ALL_CAMPGROUNDS };
	//campground view
	private static final String CAMPGROUND_MENU_OPTION_SHOW_CAMPSITES = "Show all campsites";
	private static final String CAMPGROUND_MENU_OPTION_RESERVATION = "Make a reservation";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTION_SHOW_CAMPSITES,CAMPGROUND_MENU_OPTION_RESERVATION };

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campDAO;
	private CampsiteDAO siteDAO;
	private ReservationDAO reservationDAO;
	
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
		this.siteDAO = new JDBCCampsiteDAO(datasource);
		this.reservationDAO = new JDBCReservationDAO(datasource);
	}

	public void run() {
		while (true) {
			
			handleListAndSelectParks();
		}
	}

	private void handleListAndSelectParks() {
		printHeading("Select A Park");
		List<Park> parks = parkDAO.getAllParks();
		Park parkChoice = (Park)menu.getChoiceFromOptions(parks.toArray());
		
		displayParkInfo(parkChoice);
		handleParkOption(parkChoice);
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
		Campground campgroundChoice = (Campground)menu.getChoiceFromOptions(campgrounds.toArray());
		displayCampgroundInfo(campgroundChoice, park);
		handleCampgroundOption(campgroundChoice);
	}
	
	private void handleCampgroundOption(Campground campground) {
		String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		if (choice.equals(CAMPGROUND_MENU_OPTION_SHOW_CAMPSITES)) {
			displayCampsites(campground, 1);
		}
		if (choice.equals(CAMPGROUND_MENU_OPTION_RESERVATION)) {
			checkForReservationAvailability(campground);
		}
	}

	private void checkForReservationAvailability(Campground campground) {
		LocalDate arrival = getValidReservationDate("When would you like to arrive? yyyy-mm-dd", campground);
		LocalDate departure = getValidReservationDate("When would you like to depart? yyyy-mm-dd", campground);
	    List<Campsite>availableSites = siteDAO.getAvailableCampsitesByDate(arrival, departure).subList(0, 5);
	    if (availableSites.size() == 0) {
	    		System.out.println("No sites are available for that time-frame.");
	    } else {
	    		displayCampsites(campground, arrival.until(departure).getDays());
	    		Long siteNumber = Long.parseLong(menu.getSimpleInput("Select a site: ")); //
	    		String resName = menu.getSimpleInput("Provide a name for the reservation please");
	    		Reservation reservation = new Reservation();
	    		Long siteId = 0l;
	    		for (Campsite campsite : availableSites) {
				if (Integer.parseInt(campsite.getSiteNumber()) == siteNumber) {
					siteId = campsite.getSiteId();
				}
			}
	    		reservation.setSiteId(siteId);
	    		reservation.setName(resName);
	    		reservation.setStart(arrival);
	    		reservation.setEnd(departure);
	    		int resId = reservationDAO.setReservation(reservation);
	    		System.out.println("Your reservation has been made. The confirmation number is " + resId);
	    }
	}
	
	public LocalDate getValidReservationDate(String prompt, Campground campground) {
		LocalDate date;
		while(true) {
			String dateString = menu.getSimpleInput(prompt);
			try {
				date = LocalDate.parse(dateString);
			} catch (DateTimeParseException e) {
				System.out.println("Please enter a valid date.");
				continue;
			}
			//int year = LocalDate.now().getMonthValue()+1 < campground.getOpeningTime() ? LocalDate.now().getYear() : LocalDate.now().getYear()+1;
			if (campground.getOpeningTime() > campground.getClosingTime()) {
				if ( (date.getMonthValue() >= campground.getOpeningTime()) || date.getMonthValue() <= campground.getClosingTime()) {
					break;
				}
			} else if (date.getMonthValue() >= campground.getOpeningTime()) {
				break;
			}
			System.out.println("This park is not open during " + date.getMonth());
		}
		return date;
	}

	private void displayCampgroundInfo(Campground campground, Park park) {
		printHeading(park.getName() + ": Campground Information");
		System.out.println(campground.getName());
		System.out.println("Opens In: " + campground.getOpeningMonth());
		System.out.println("Closes In: " + campground.getClosingMonth());
		System.out.println("Daily Fee: " + campground.getDailyFee());
		System.out.println();
	}
	
	private void displayCampsites(Campground campground, int stayLength) {
		printHeading("Campsites in " + campground.getName());
		System.out.format("%15s%15s%15s%15s%15s%15s", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utility", "Cost");
		List<Campsite> topTenSites =siteDAO.getAllCampsitesForCampground(campground.getId()).subList(0, 10);
		for (Campsite site : topTenSites) {
			System.out.format("%75s%15s", site.toString(), NumberFormat.getCurrencyInstance().format((float)campground.getDailyFee()*stayLength));
			System.out.println("");
		}
	}


	private void printHeading(String headingText) {
		System.out.println("\n\b" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
