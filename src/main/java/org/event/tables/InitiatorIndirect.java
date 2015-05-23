package org.event.tables;

import java.util.List;

import org.event.tables.model.Guest;
import org.event.tables.model.Table;
import org.event.tables.model.constraints.Avoid;
import org.event.tables.model.constraints.AvoidPlacement;
import org.event.tables.model.constraints.Follow;
import org.event.tables.model.constraints.ForcePlacement;

public class InitiatorIndirect extends Initiator {
	
	@Override
	protected void initTablesDefinitions() {
	}
	public void setTablesDefinitions(List<Table> tables) {
		this.tables = tables;
	}

	@Override
	protected void initGuestsDefinitions() {
	}
	public void setGuests(List<Guest> guests) {
		this.guests = (Guest[])guests.toArray();
	}

	@Override
	protected void initAvoids() {
	}
	public void setAvoids(List<Avoid> avoids) {
		this.avoids = avoids;
	}

	@Override
	protected void initFollows() {
	}
	public void setFollows(List<Follow> follows) {
		this.follows = follows;
	}

	@Override
	protected void initAvoidPlacements() {
	}
	public void setAvoidPlacements(List<AvoidPlacement> avoidPlacements) {
		this.avoidPlacements = avoidPlacements;
	}

	@Override
	protected void initForcePlacements() {
	}
	public void setForcePlacements(List<ForcePlacement> forcePlacements) {
		this.forcePlacements = forcePlacements;
	}

}
