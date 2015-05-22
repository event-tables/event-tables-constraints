package org.event.tables.model.constraints;

public abstract class Constraint {
	
	Constraint(int guestIndex1) {
		this.guestIndex1 = guestIndex1;
	}
	
	private int guestIndex1;
	public void setGuestIndex1(int guestIndex1) {
		this.guestIndex1 = guestIndex1;
	}
	public int getGuestIndex1() {
		return this.guestIndex1;
	}

}
