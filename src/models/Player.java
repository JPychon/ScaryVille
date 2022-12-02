package models;

import models.Coordinate.coordinateType;

public class Player {
		
	private boolean isAlive = false; // Player status
	private Coordinate currentLocation = new Coordinate(coordinateType.PLAYER); // Player location
	
	public Player() { }
	
	public boolean getIsAlive() { return isAlive; } // Returns player status
	public Coordinate getCurrentLocation() { return currentLocation; } // Returns player location

	public void setIsAlive(boolean alive) { isAlive = alive; } // Sets player status
	
	public void setCurrentLocation(Coordinate location) {
		currentLocation.setRow(location.getRow());
		currentLocation.setColumn(location.getColumn());
	}
}
