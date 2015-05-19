package org.stavros.event.tables.model;

public class Table {
	
	public Table(String name, int seats) {
		this.name = name;
		this.seats = seats;
	}
	
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	
	private int seats;
	public void setSeats(int seats) {
		this.seats = seats;
	}
	public int getSeats() {
		return this.seats;
	}

}
