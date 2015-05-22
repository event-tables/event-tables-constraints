package org.event.tables.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.event.tables.Initiator;
import org.event.tables.model.Guest;
import org.event.tables.model.Table;
import org.event.tables.model.constraints.Avoid;
import org.event.tables.model.constraints.AvoidPlacement;
import org.event.tables.model.constraints.Follow;
import org.event.tables.model.constraints.ForcePlacement;

public class InitiatorFileSimple extends Initiator {

	@Override
	protected List<Table> getTablesDefinitions() {
		List<Table> tables = new ArrayList<>();
		File file = new File(getClass().getClassLoader().getResource("tables.csv").getFile());
		try (Scanner scanner = new Scanner(file);) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] fields = line.split(",");
				tables.add(new Table(fields[0], Integer.valueOf(fields[1].trim())));
			}
		}
		catch(FileNotFoundException fnfe) {
			LOGGER.error(fnfe.getMessage(), fnfe);
		}
		return tables;
	}

	@Override
	protected Guest[] getGuestsDefinitions() {
		List<Guest> listGuests = new ArrayList<>();
		File file = new File(getClass().getClassLoader().getResource("guests.csv").getFile());
		try (Scanner scanner = new Scanner(file);) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] fields = line.split(",");
				listGuests.add(new Guest(Integer.valueOf(fields[0].trim()), fields[1]));
			}
		}
		catch(FileNotFoundException fnfe) {
			LOGGER.error(fnfe.getMessage(), fnfe);
		}
		
		Guest[] guests = (Guest[])listGuests.toArray();
		return guests;
	}

	@Override
	protected List<Avoid> getAvoids() {
		List<Avoid> avoids = new ArrayList<>();
		File file = new File(getClass().getClassLoader().getResource("avoids.csv").getFile());
		try (Scanner scanner = new Scanner(file);) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] fields = line.split(",");
				avoids.add(new Avoid(Integer.valueOf(fields[0].trim()), Integer.valueOf(fields[1].trim())));
			}
		}
		catch(FileNotFoundException fnfe) {
			LOGGER.error(fnfe.getMessage(), fnfe);
		}
		return avoids;
	}

	@Override
	protected List<Follow> getFollows() {
		List<Follow> follows = new ArrayList<>();
		File file = new File(getClass().getClassLoader().getResource("follows.csv").getFile());
		try (Scanner scanner = new Scanner(file);) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] fields = line.split(",");
				follows.add(new Follow(Integer.valueOf(fields[0].trim()), Integer.valueOf(fields[1].trim())));
			}
		}
		catch(FileNotFoundException fnfe) {
			LOGGER.error(fnfe.getMessage(), fnfe);
		}
		return follows;
	}
	
	@Override
	protected List<AvoidPlacement> getAvoidPlacements() {
		List<AvoidPlacement> avoidPlacements = new ArrayList<>();
		return avoidPlacements;
	}
	
	@Override
	protected List<ForcePlacement> getForcePlacements() {
		List<ForcePlacement> forcePlacements = new ArrayList<>();
		return forcePlacements;
	}

}