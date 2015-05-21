package org.stavros.event.tables.model.constraints;

public abstract class Constraint {
	
	Constraint(int guestIndex1, int guestIndex2) {
		this.guestIndex1 = guestIndex1;
		this.guestIndex2 = guestIndex2;
	}
	
	private int guestIndex1;
	public void setGuestIndex1(int guestIndex1) {
		this.guestIndex1 = guestIndex1;
	}
	public int getGuestIndex1() {
		return this.guestIndex1;
	}
	
	private int guestIndex2;
	public void setGuestIndex2(int guestIndex2) {
		this.guestIndex2 = guestIndex2;
	}
	public int getGuestIndex2() {
		return this.guestIndex2;
	}

}
