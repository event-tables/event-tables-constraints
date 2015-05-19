package org.stavros.event.tables.model;

public class Guest {
	
	public Guest(int index, String name) {
		this.name = name;
	}
	
	private int index;
	public void setIndex(int index) {
		this.index = index;
	}
	public int getIndex() {
		return this.index;
	}
	
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

}
