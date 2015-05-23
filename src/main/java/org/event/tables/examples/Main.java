package org.event.tables.examples;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.event.tables.model.PlaceAssignment;
import org.event.tables.model.Solution;


public class Main {
	
	public final static Logger LOGGER = LogManager.getLogger(Main.class);
	
	public static void main(String[] args) {
		InitiatorCode main = new InitiatorCode();
		main.setCustomNumberOfGuests(105);
		main.setCustomNumberOfTables(11);
		main.setCustomNumberOfSeatsPerTable(10);
		
		Solution solution = main.getFirstSolution();
		
		for (PlaceAssignment pa: solution.getPlaceAssignments()) {
			LOGGER.info(pa.getGuest().getName() + " -- " + pa.getTable().getName());
		}
	}

}
