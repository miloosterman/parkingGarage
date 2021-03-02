import java.text.NumberFormat;
import java.util.Scanner;

//Milo Osterman
//parkingGarage.java
//CIS129
//This program allows a user to input the information about their parking and receive the results.

public class parkingGarage {
	static Scanner keyboard = new Scanner(System.in);
	//Declare constants from specifications
	final static int PARKING_INTERVAL = 15;
	final static double WEEK_DAY_RATE = 1.25;
	final static double WEEKEND_RATE = 0.50;
	final static int MINS_IN_HR = 60;
	final static int HRS_IN_DAY = 24;
	final static double MAX_CHARGE = 15.00;
	
	//Main
	public static void main(String[] args) {
		String dayOfWeek;
		int arrivalTime;
		int departureTime;
		double parkingRate;
		int parkingDuration;
		double amtCharged;
		
		dayOfWeek = getDayOfWeek("Please enter the day of the week: "); //Get day of week from user
		parkingRate = getParkingRate(dayOfWeek); //Get rate based on day of week
		do {
			arrivalTime = getTime("Please enter the vehicle’s arrival time: "); //Get arrival time
			departureTime = getTime("Please enter the vehicle's departure time: "); //Get departure time
		} while (!timeIsCompatible(arrivalTime, departureTime)); //Make sure arrival time isn't after departure
		parkingDuration = getParkingDuration(departureTime, arrivalTime); //Calculate parking duration in minutes
		amtCharged = getAmtCharged(parkingDuration, parkingRate); //Calculate the amount charged for parking
		
		displayResults(dayOfWeek, parkingDuration, parkingRate, amtCharged); //Use above variables to display results
		
	}
	
	//Getting the day from user...
	public static String getDayOfWeek(String msg) {
		String dayOfWeek;
		System.out.println(msg);
		
		do {
			dayOfWeek = keyboard.nextLine();
		} while (!dayIsValid(dayOfWeek)); //Check for validity with validation loop
		
		return dayOfWeek;
		
	}
	
	//Valid options for user to select day of week
	public static Boolean dayIsValid(String day) {
		while (day.compareToIgnoreCase("mon") != 0
				&& day.compareToIgnoreCase("tue") != 0
				&& day.compareToIgnoreCase("wed") != 0
				&& day.compareToIgnoreCase("thu") != 0
				&& day.compareToIgnoreCase("fri") != 0
				&& day.compareToIgnoreCase("sat") != 0
				&& day.compareToIgnoreCase("sun") != 0) {
					System.out.println("You did not enter the day in a valid format. \n"
							+ "Please enter the first three letters of the day.");
					return false; //Return false making user enter date again
					
		}
		
		return true; //If input passes validation, continue on with true
			
		}
	
	//Check if it is a weekend or week day and return the correct rate
	public static double getParkingRate(String day) {
		if (day.equals("sat") || day.equals("sun")) {
			return WEEKEND_RATE;
			
		}
		
		return WEEK_DAY_RATE;
		
	}
	
	/*Function that accepts input from user for both arrival and departure times
	Two part process of accepting hour and minutes in order to stay within bounds*/
	public static int getTime(String msg) {
		String time;
		int timeInMinutes;
		
		System.out.println(msg);
		do {
			time = getValidEntry("Please enter the time in XXXX 24 hour format.");
		} while (!checkTimeWithinRange(time) || !checkTimeBounds(time));
		
		System.out.println("You entered: " + time);
		
		timeInMinutes = convertTimeToMins(time);
		
		return timeInMinutes;
		
	}
	
	//Checks the input received in getTime function to make sure they are within range
	public static boolean checkTimeBounds(String time) {
		int hours;
		int minutes;
		
		hours = Integer.parseInt(time.substring(0,2));
		minutes = Integer.parseInt(time.substring(2,4));
		
		if (hours > HRS_IN_DAY || hours < 0) { //Check that hours are within 0 and 24
			System.out.println("You entered the hour outside of bounds.");
			return false;
			
		}
		
		if (minutes >= MINS_IN_HR || minutes < 0) {
			System.out.println("You entered the minutes outside of bounds.");
			return false;
			
		}
		
		if (hours >= 24) {
			System.out.println("Parking is unavailable past midnight.");
			return false;
		}
		
		return true;
		
	}
	
	//Verify that the arrival time is before departure time
	public static boolean timeIsCompatible(int arrivalTime, int departureTime) {
		if (arrivalTime > departureTime) {
			System.out.println("You cannot have an arrival time later than departure.");
			return false;
			
		}
		
		return true;
		
	}
	
	//Use departure and arrival time to get total parking duration
	public static int getParkingDuration(int departureTime, int arrivalTime) {
		int parkingDuration;
		
		parkingDuration = departureTime - arrivalTime;
		
		return parkingDuration;
		
	}
	
	//Calculate amount charged with parking duration & rate. Add additional interval if parking was over 15 minute interval.
	public static double getAmtCharged(int parkingDuration, double parkingRate) {
		double amtCharged;
		int numOfIntervals;
		
		numOfIntervals = parkingDuration / PARKING_INTERVAL;
		if ((parkingDuration % PARKING_INTERVAL) > 0) {
			numOfIntervals += 1;
			
		}
		amtCharged = (numOfIntervals * parkingRate);
		
		if (amtCharged > MAX_CHARGE) {
			amtCharged = MAX_CHARGE;
					
		}
		
		return amtCharged;
		
	}

	//Shows the results of the users parking and how much they need to pay.
	public static void displayResults(String dayOfWeek, int parkingDuration,
			double parkingRate, double amtCharged) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		dayOfWeek = dayOfWeek.substring(0,1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
		System.out.println("Day of week: " + dayOfWeek);
		System.out.println("Parking duration in minutes: " + parkingDuration + ", rate: " + formatter.format(parkingRate));
		System.out.println("Amount charged: " + formatter.format(amtCharged));
	}
	
	//Make sure the time that user enters falls within required bounds. Add 0 for right format if only 3 digits.
	public static String getValidEntry(String msg) {
		int number;
		String stringNumber;
      	System.out.println(msg);
      	do {
      	while (!keyboard.hasNextInt()) { 
      		keyboard.nextLine();
      		System.err.println("Invalid entry. Try again.");
      	}
      	number = keyboard.nextInt();
      	keyboard.nextLine();
      	stringNumber = Integer.toString(number);
      	if (stringNumber.length() == 3) {
      		stringNumber = "0" + stringNumber;
      	}
      	if (number < 0) {
      		System.err.println("Cannot enter a number less than zero.");
      	}
      	} while (number < 0);

      	return stringNumber;
      	
   	}

	//Convert the time of stay into minutes by separating the hours and mins with substring
	public static int convertTimeToMins(String time) {
		int hours;
		int minutes;
		int timeInMins;
		
		hours = Integer.parseInt(time.substring(0,2));
		minutes = Integer.parseInt(time.substring(2,4));
		
		timeInMins = (hours * 60) + minutes;
		
		return timeInMins;
	
	}
	
	//Validates that the time entered by user is the proper XXXX format by checking that there are 4 characters.
	public static boolean checkTimeWithinRange(String time) {
		
		if (time.length() != 4) {
			return false;
			
		}
		
		return true;
	
	}

}
