package org.stavros.event.tables.model.constraints;

public class Social extends Constraint {

	Social(int guestIndex1, int guestIndex2) {
		super(guestIndex1);
		setGuestIndex2(guestIndex2);
	}
	
	private int guestIndex2;
	public void setGuestIndex2(int guestIndex2) {
		this.guestIndex2 = guestIndex2;
	}
	public int getGuestIndex2() {
		return this.guestIndex2;
	}
}
