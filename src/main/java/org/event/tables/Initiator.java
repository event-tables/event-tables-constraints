package org.event.tables;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.Variable;
import org.event.tables.examples.Main;
import org.event.tables.model.Guest;
import org.event.tables.model.Table;
import org.event.tables.model.constraints.Avoid;
import org.event.tables.model.constraints.AvoidPlacement;
import org.event.tables.model.constraints.Follow;
import org.event.tables.model.constraints.ForcePlacement;

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
	
	protected abstract List<Table> getTablesDefinitions();
	
	protected abstract Guest[] getGuestsDefinitions();
	
	protected abstract List<Avoid> getAvoids();
	
	protected abstract List<Follow> getFollows();
	
	protected abstract List<AvoidPlacement> getAvoidPlacements();
	
	protected abstract List<ForcePlacement> getForcePlacements();
	
	protected int getIndex(String val) {
		return Integer.valueOf(val.substring(val.indexOf('[')+1, val.indexOf(']')));
	}
	
	public void go() {
		// initialize tables
		List<Table> tables = getTablesDefinitions();
		int availableSeats = getAvailableSeats(tables);
		int numberOfTables = tables.size();
		
		// initialize guests
		Guest[] guests = getGuestsDefinitions();
		int numberOfGuests = guests.length;
		
		// validation
		if (availableSeats != numberOfGuests) {
			LOGGER.error("The aggregated number of seats of tables is different from the number of guests");
			if (numberOfGuests > availableSeats) {
				LOGGER.error("The number of guests is bigger than the number of aggregated table seats");
				return;
			}
			else {
				LOGGER.error("The aggregated number of seats of tables is bigger than the number of guests... adding " + (availableSeats-numberOfGuests) + " default guests (empty seats)");
				Guest[] newGuests = Arrays.copyOf(guests, availableSeats);
				for (int i=numberOfGuests; i<availableSeats; i++) {
					newGuests[i] = new Guest(i, "emptySeat");
				}
				guests = newGuests;
				numberOfGuests = availableSeats;
			}
		}
		
		// the number of guests should be equal to the number of available seats
		
		Solver solver = new Solver();
		
		// one variable for each guest
		// each one of the guests variables can take a value between 1 and the number of tables
		IntVar[] guestsVar = VF.enumeratedArray(guestsArrayPrefix, numberOfGuests, 1, numberOfTables, solver);
		
		int i=0;
		for (Table table: tables) {
			IntVar positions = VF.enumerated("positions_"+table.getName(), new int[]{table.getSeats()}, solver);
			IntVar tableNumber = VF.enumerated(table.getName(), new int[]{++i}, solver);
			solver.post(ICF.count(tableNumber, guestsVar, positions));
		}
		
		// Apply the guests avoid constraints (guest avoids another guest)
		for (Avoid avoid: getAvoids()) {
			solver.post(ICF.alldifferent(new IntVar[]{guestsVar[avoid.getGuestIndex1()], guestsVar[avoid.getGuestIndex2()]}));
		}
		
		// Apply the guests follow constraints (guest follows another guest)
		for (Follow follow: getFollows()) {
			solver.post(ICF.absolute(guestsVar[follow.getGuestIndex1()], guestsVar[follow.getGuestIndex2()]));
		}
		
		// Apply the avoid placement constraints (guest avoids placement to a specific table)
		for (AvoidPlacement avoidPlacement: getAvoidPlacements()) {
			solver.post(ICF.alldifferent(new IntVar[]{guestsVar[avoidPlacement.getGuestIndex1()], (IntVar)getVariableValue(solver, avoidPlacement.getTableName())}));
		}
		
		// Apply the force placement constraints (guest forced placement to a specific table)
		for (ForcePlacement forcePlacement: getForcePlacements()) {
			solver.post(ICF.absolute(guestsVar[forcePlacement.getGuestIndex1()], (IntVar)getVariableValue(solver, forcePlacement.getTableName())));
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
	
	private int getTableOrderIndex(List<Table> tables, String tableName) {
		int ret = 0;
		int i = 0;
		for (Table table: tables) {
			if (tableName.equals(table.getName())) {
				ret = i;
				break;
			}
			i++;
		}
		
		if (ret == 0) {
			throw new IllegalStateException();
		}
		return ret;
	}
	
	private Variable getVariableValue(Solver solver, String variableName) {
		Variable ret = null;
		for (Variable var: solver.getVars()) {
			if (variableName.equals(var.getName())) {
				ret = var;
				break;
			}
		}
		return ret;
	}

}
