package org.stavros.event.tables.examples;


public class Main {
	
	public static void main(String[] args) {
		InitiatorCode main = new InitiatorCode();
		main.setCustomNumberOfGuests(105);
		main.setCustomNumberOfTables(11);
		main.setCustomNumberOfSeatsPerTable(10);
		
		main.go();
	}

}
