package org.event.tables.model;

public class PlaceAssignment {
	
	public PlaceAssignment(Table table, Guest guest) {
		this.table = table;
		this.guest = guest;
	}
	
	private Guest guest;
	public void setGuest(Guest guest) {
		this.guest = guest;
	}
	public Guest getGuest() {
		return this.guest;
	}
	
	private Table table;
	public void setTable(Table table) {
		this.table = table;
	}
	public Table getTable() {
		return this.table;
	}

}
