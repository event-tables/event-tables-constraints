package org.event.tables.model.constraints;

public class Placement extends Constraint {
	public Placement(int guestIndex1, String tableName) {
		super(guestIndex1);
		setTableName(tableName);
	}
	
	private String tableName;
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableName() {
		return this.tableName;
	}
}
