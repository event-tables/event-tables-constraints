package org.stavros.event.tables;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.Variable;
import org.stavros.event.tables.model.Avoid;
import org.stavros.event.tables.model.Follow;
import org.stavros.event.tables.model.Guest;
import org.stavros.event.tables.model.Table;

public abstract class Initiator {
	
	public final static Logger LOGGER = LogManager.getLogger(Main.class);
	
	private final static String guestsArrayPrefix = "guests";
	
	protected int getAvailableSeats(List<Table> tables) {
		int availableSeats = 0;
		for (Table table: tables) {
			availableSeats+=table.getSeats();
		}
		return availableSeats;
	}
	
	protected abstract List<Table> getTablesDefinitions(int numberOfTables, int numberOfSeatsPerTable);
	
	protected abstract Guest[] getGuestsDefinitions(int numberOfGuests);
	
	protected abstract List<Avoid> getAvoids();
	
	protected abstract List<Follow> getFollows();
	
	protected int getIndex(String val) {
		return Integer.valueOf(val.substring(val.indexOf('[')+1, val.indexOf(']')));
	}
	
	public void go() {
		// initialize guests
		int numberOfGuests = 100;
		Guest[] guests = getGuestsDefinitions(numberOfGuests);
		
		// initialize tables
		int numberOfTables = 10;
		List<Table> tables = getTablesDefinitions(numberOfTables, 10);
		
		// validation
		int availableSeats = getAvailableSeats(tables);
		if (availableSeats != numberOfGuests) {
			LOGGER.error("The aggregated number of seats of tables is different from the number of guests");
			return;
		}
		
		Solver solver = new Solver();
        IntVar[] guestsVar = VF.enumeratedArray(guestsArrayPrefix, numberOfGuests, 1, numberOfTables, solver);
        
        int i=0;
        for (Table table: tables) {
        	IntVar positions = VF.enumerated("positions_"+table.getName(), new int[]{table.getSeats()}, solver);
        	IntVar tableNumber = VF.enumerated(table.getName(), new int[]{++i}, solver);
        	solver.post(ICF.count(tableNumber, guestsVar, positions));
        }
        
        for (Avoid avoid: getAvoids()) {
        	solver.post(ICF.alldifferent(new IntVar[]{guestsVar[avoid.getGuestIndex1()], guestsVar[avoid.getGuestIndex2()]}));
        }
        
        for (Follow follow: getFollows()) {
        	solver.post(ICF.absolute(guestsVar[follow.getGuestIndex1()], guestsVar[follow.getGuestIndex2()]));
        }
        
        if(solver.findSolution()){
    	   //do{
    	       for (Variable var: solver.getVars()) {
    	    	   if (var instanceof IntVar) {
    	    		   if (var.getName().startsWith(guestsArrayPrefix)) {
    	    			   int index = getIndex(var.getName());
    	    			   LOGGER.info(guests[index].getName() + ":\t" + ((IntVar)var).getValue());
    	    		   }
    	    	   }
    	       }
    	       for (Variable var: solver.getVars()) {
    	    	   if (var instanceof IntVar) {
    	    		   if (!var.getName().startsWith(guestsArrayPrefix)) {
    	    			   LOGGER.info(var.getName() + ": " + ((IntVar)var).getValue());
    	    		   }
    	    	   }
    	       }
    	   //}while(solver.nextSolution());
    	}
	}

}
