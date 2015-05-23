package org.event.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.Variable;
import org.event.tables.model.Guest;
import org.event.tables.model.PlaceAssignment;
import org.event.tables.model.Solution;
import org.event.tables.model.Table;
import org.event.tables.model.constraints.Avoid;
import org.event.tables.model.constraints.AvoidPlacement;
import org.event.tables.model.constraints.Follow;
import org.event.tables.model.constraints.ForcePlacement;

public abstract class Initiator {
	
	public final static Logger LOGGER = LogManager.getLogger(Initiator.class);
	
	private final static String guestsArrayPrefix = "guests";
	
	protected int getAvailableSeats() {
		int availableSeats = 0;
		for (Table table: tables) {
			availableSeats+=table.getSeats();
		}
		return availableSeats;
	}
	
	protected abstract void initTablesDefinitions();
	
	protected abstract void initGuestsDefinitions();
	
	protected abstract void initAvoids();
	
	protected abstract void initFollows();
	
	protected abstract void initAvoidPlacements();
	
	protected abstract void initForcePlacements();
	
	protected int getIndex(String val) {
		return Integer.valueOf(val.substring(val.indexOf('[')+1, val.indexOf(']')));
	}
	
	private Solver solver;
	protected List<Table> tables;
	protected final List<Table> getTables() {
		return this.tables;
	}
	protected Guest[] guests;
	protected final Guest[] getGuests() {
		return this.guests;
	}
	protected List<Avoid> avoids;
	protected final List<Avoid> getAvoids() {
		return this.avoids;
	}
	protected List<Follow> follows;
	protected final List<Follow> getFollows() {
		return this.follows;
	}
	protected List<AvoidPlacement> avoidPlacements;
	protected final List<AvoidPlacement> getAvoidPlacements() {
		return this.avoidPlacements;
	}
	protected List<ForcePlacement> forcePlacements;
	protected final List<ForcePlacement> getForcePlacements() {
		return this.forcePlacements;
	}
	
	private void go() {
		// initialize tables
		initTablesDefinitions();
		int availableSeats = getAvailableSeats();
		int numberOfTables = tables.size();
		
		// initialize guests
		initGuestsDefinitions();
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
		
		this.solver = new Solver();
		
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
		initAvoids();
		if (getAvoids() != null) {
			for (Avoid avoid: getAvoids()) {
				solver.post(ICF.alldifferent(new IntVar[]{guestsVar[avoid.getGuestIndex1()], guestsVar[avoid.getGuestIndex2()]}));
			}
		}
		
		// Apply the guests follow constraints (guest follows another guest)
		initFollows();
		if (getFollows() != null) {
			for (Follow follow: getFollows()) {
				solver.post(ICF.absolute(guestsVar[follow.getGuestIndex1()], guestsVar[follow.getGuestIndex2()]));
			}
		}
		
		// Apply the avoid placement constraints (guest avoids placement to a specific table)
		initAvoidPlacements();
		if (getAvoidPlacements() != null) {
			for (AvoidPlacement avoidPlacement: getAvoidPlacements()) {
				solver.post(ICF.alldifferent(new IntVar[]{guestsVar[avoidPlacement.getGuestIndex1()], (IntVar)getVariableValue(avoidPlacement.getTableName())}));
			}
		}
		
		// Apply the force placement constraints (guest forced placement to a specific table)
		initForcePlacements();
		if (getForcePlacements() != null) {
			for (ForcePlacement forcePlacement: getForcePlacements()) {
				solver.post(ICF.absolute(guestsVar[forcePlacement.getGuestIndex1()], (IntVar)getVariableValue(forcePlacement.getTableName())));
			}
		}
	}
	
	public Solution getFirstSolution() {
		go();
		
		List<PlaceAssignment> placeAssignments = new ArrayList<>();
		if(solver.findSolution()){
			//do{
				for (Variable var: solver.getVars()) {
					if (var instanceof IntVar) {
						if (var.getName().startsWith(guestsArrayPrefix)) {
							Table table = getTableByValue(((IntVar)var).getValue());
							int index = getIndex(var.getName());
							Guest guest = guests[index];
							PlaceAssignment pa = new PlaceAssignment(table, guest);
							placeAssignments.add(pa);
						}
					}
				}
			//}while(solver.nextSolution());
		}
		
		Solution solution = new Solution();
		solution.setPlaceAssignments(placeAssignments);
		return solution;
	}
	
	public List<Solution> getSolutions() {
		go();
		
		List<Solution> solutions = new ArrayList<>();
		
		if(solver.findSolution()){
			do{
				List<PlaceAssignment> placeAssignments = new ArrayList<>();
				for (Variable var: solver.getVars()) {
					if (var instanceof IntVar) {
						if (var.getName().startsWith(guestsArrayPrefix)) {
							Table table = getTableByValue(((IntVar)var).getValue());
							int index = getIndex(var.getName());
							Guest guest = guests[index];
							PlaceAssignment pa = new PlaceAssignment(table, guest);
							placeAssignments.add(pa);
						}
					}
				}
				Solution solution = new Solution();
				solution.setPlaceAssignments(placeAssignments);
				solutions.add(solution);
			} while(solver.nextSolution());
		}
		
		return solutions;
	}
	
	private Table getTableByValue(int value) {
		Table ret = null;
		for (Table table: tables) {
			if (((IntVar)getVariableValue(table.getName())).getValue() == value) {
				ret = table;
				break;
			}
		}
		return ret;
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
	
	private Variable getVariableValue(String variableName) {
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
