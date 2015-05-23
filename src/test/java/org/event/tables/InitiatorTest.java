package org.event.tables;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.event.tables.model.Guest;
import org.event.tables.model.PlaceAssignment;
import org.event.tables.model.Solution;
import org.event.tables.model.Table;
import org.junit.Test;

public class InitiatorTest {
	
	@Test
	public void testSimple() {
		List<Guest> guests = new ArrayList<>();
		guests.add(new Guest(0, "test1"));
		guests.add(new Guest(0, "test2"));
		
		List<Table> tables = new ArrayList<>();
		tables.add(new Table("tableName", 4));
		
		InitiatorIndirect test = new InitiatorIndirect();
		test.setTablesDefinitions(tables);
		test.setGuests(guests);
		
		Solution solution = test.getFirstSolution();
		
		for (PlaceAssignment pa: solution.getPlaceAssignments()) {
			assertEquals("tableName", pa.getTable().getName());
		}
	}

}
