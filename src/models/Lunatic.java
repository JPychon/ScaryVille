package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controllers.GameController;
import controllers.LunaticController;
import models.Coordinate.coordinateType;

public class Lunatic {

	private Coordinate spawnLocation = new Coordinate(coordinateType.LUNATIC); // Starting point
	private Coordinate currentLocation;
	private Coordinate currentDestination;
	
	private int currentPathIterator;
	private int lunatic_id;
	private boolean isFrozen = true; // Disables or enables movement/control
	
	private List<List<Coordinate>> line_of_sight = new ArrayList<List<Coordinate>>(); // 2D list for both vertical & horizontal line of sight
	
	private List<Coordinate> left_horizontal_line_of_sight = new ArrayList<Coordinate>(); // List of columns visible left of the lunatic
	private List<Coordinate> right_horizontal_line_of_sight = new ArrayList<Coordinate>(); // List of columns visible right of the lunatic
	
	private List<Coordinate> up_vertical_line_of_sight = new ArrayList<Coordinate>(); // List of rows visible over the lunatic
	private List<Coordinate> down_vertical_line_of_sight = new ArrayList<Coordinate>(); // List of rows visible under the lunatic
	
	private List<Coordinate> pathToPlayer = new ArrayList<Coordinate>(); // List to hold the (row, col) values of the path to the player location
	
	private lunaticState state;
	public enum lunaticState {
		idle_roam,
		investigating,
		chasing
	}
	
	public lunaticNextMove move;
	public enum lunaticNextMove {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		IDLE // No movement
	}
	
	public Lunatic() 
		{
			lunatic_id = LunaticController.LUNATIC_ID_INDEX; // Assigns a new ID to this instance through the controller ID index.
			isFrozen = false; // Enables controls
			currentLocation = spawnLocation;
			pathToPlayer = new ArrayList<Coordinate>();
		}
	
	private static final Random randomDirection = new Random(); // Random instance for picking the next direction
	
	private static lunaticNextMove nextMove() { // Static method to pick the next move.
		lunaticNextMove[] moves = lunaticNextMove.values(); // Creates an array of the enum values
		return moves[randomDirection.nextInt(moves.length)]; // Picks a random value based on the enum-length & returns it.
	}
	
	public lunaticNextMove roam(int currentRow, int currentCol)  // Returns the next lunatic move in a walkable cell.
	{ 
		lunaticNextMove move = lunaticNextMove.IDLE; // Default case
		if(!isFrozen && GameController.isLost == false && GameController.isPaused == false) 
		{
			boolean isWalkable = false;
			while(!isWalkable) // Don't return a value until the next move is walkable.
			{
				move = Lunatic.nextMove(); // Gets a random value from the move enum
				isWalkable = GridPathFinder.IsWalkable(currentRow-1, currentCol, move); // Checks if the cell corresponding with the move is possible.
			}
		}
		return move;
	}
	
	public int getPathIterator() { return currentPathIterator; }
	public int getLunaticID() { return lunatic_id; }
	
	public List<Coordinate> getPathToPlayer() { return pathToPlayer; }
	
	public Coordinate getCurrentDestination() { return currentDestination; }
	public Coordinate getSpawnLocation() { return spawnLocation; }
	
	public boolean getControlStatus() { return isFrozen; }
	public List<List<Coordinate>> getLineOfSight() { return line_of_sight; }
	public Coordinate getCurrentLocation() { return currentLocation; }
	
	public List<Coordinate> getHorizontalLineOfSight_left() { return left_horizontal_line_of_sight;}
	public List<Coordinate> getHorizontalLineOfSight_right() { return right_horizontal_line_of_sight;}
	
	public List<Coordinate> getVerticalLineOfSight_up() { return up_vertical_line_of_sight; }
	public List<Coordinate> getVerticalLineOfSight_down() { return down_vertical_line_of_sight; }
	
	public lunaticState getLunaticState() { return state; }
	
	public void setPathIterator(int iterator) { currentPathIterator = iterator; }
	public void setLunaticID(int id) { lunatic_id = id; }
	public void setPathToPlayer(List<Coordinate> path) { pathToPlayer = path; }
	public void setCurrentDestination(Coordinate destination) { currentDestination = destination; }
	public void setControlStatus(boolean frozen) { isFrozen = frozen; }
	public void setLineOfSight(List<List<Coordinate>> los) { line_of_sight = los; }
	
	public void setHorizontalLineOfSight_left(List<Coordinate> h_los) { left_horizontal_line_of_sight = h_los; }
	public void setHorizontalLineOfSight_right(List<Coordinate> h_los) { right_horizontal_line_of_sight = h_los; }
	
	public void setVerticalLineOfSight_up(List<Coordinate> v_los) { up_vertical_line_of_sight = v_los; }
	public void setVerticalLineOfSight_down(List<Coordinate> v_los) { down_vertical_line_of_sight = v_los; }
	
	public void setLunaticState(lunaticState lState) { state = lState; }
	
	public void setCurrentLocation(int row, int col) 
	{
		currentLocation.setRow(row);
		currentLocation.setColumn(col);
	}
	
	public void setCurrentLocation(Coordinate location)
	{
		currentLocation.setRow(location.getRow());
		currentLocation.setColumn(location.getColumn());
	}	
	
	public void setSpawnLocation(int row, int col) {
		spawnLocation.setRow(row);
		spawnLocation.setColumn(col);
	}
	
	public void setSpawnLocation(Coordinate spawn) {
		spawnLocation.setRow(spawn.getRow());
		spawnLocation.setColumn(spawn.getColumn());
	}
	
	
	public Coordinate getNextMoveToPlayer() 
	{
	    if (pathToPlayer == null || pathToPlayer.size() == 0) // Check if the path is empty or not valid
	    {
	        pathToPlayer = GridPathFinder.FindPath(currentLocation, GameController.PLAYER_CONTROLLER.getPlayerLocation()); // Recalculate the path to the player's current location
	    }
	    if (pathToPlayer == null || pathToPlayer.size() == 0) // If the path is still empty or not valid, return the current location
	    {
	        return currentLocation;  
	    } 
	    else 
	    {
	        Coordinate nextPath = pathToPlayer.get(0);  // Return the next coordinate on the path
	        pathToPlayer.remove(0);
	        return nextPath;
	    }
	}
	
	public void updateLunaticState(Coordinate currentPlayerLocation) {
		
		updateLunaticLOS();
		if(state == lunaticState.idle_roam)  // ROAMING
		{
			if(checkLOS(currentPlayerLocation)) // LOS
			{
				setCurrentDestination(currentPlayerLocation); // Sets the destination to the player-sighting location.
				pathToPlayer = GridPathFinder.FindPath(currentLocation, currentDestination); // Find the clear path to the player location.
				state = lunaticState.chasing; // Set state to chasing
			}
			if(!checkLOS(currentPlayerLocation)) // NO-LOS
			{
				// Do nothing - keep roaming.
			}
		}
		if(state == lunaticState.chasing)  // CHASING
		{
			if(checkLOS(currentPlayerLocation)) // LOS
			{
				setCurrentDestination(currentPlayerLocation); // Sets the destination to the NEW player-sighting location
				pathToPlayer = GridPathFinder.FindPath(currentLocation, currentDestination); // Find the clear path to the NEW player location.
			}
			
			if(!checkLOS(currentPlayerLocation)) // NO-LOS
			{
				state = lunaticState.investigating; // Investigate the last-sighting location.
			}
		}
		if(state == lunaticState.investigating) // INVESTIGATING
		{
			if(checkLOS(currentPlayerLocation)) // LOS
			{
				setCurrentDestination(currentPlayerLocation); // Sets the destination to the NEW player-sighting location
				pathToPlayer = GridPathFinder.FindPath(currentLocation, currentDestination); // Find the clear path to the NEW player location
				state = lunaticState.chasing; // Set state to chasing
			}
			
			if(!checkLOS(currentPlayerLocation)) // NO-LOS
			{
				if(pathToPlayer.size() == 0) // If reached the last point of sighting, reset to roaming.
				{ 
					state = lunaticState.idle_roam;
				}
			}
		}
	}
	
	public boolean checkLOS(Coordinate playerLocation) 
	{
		List<List<Coordinate>> linesOfSight = new ArrayList<>();
		linesOfSight.add(getHorizontalLineOfSight_left());
		linesOfSight.add(getHorizontalLineOfSight_right());
		linesOfSight.add(getVerticalLineOfSight_up());
		linesOfSight.add(getVerticalLineOfSight_down());

		for (List<Coordinate> lineOfSight : linesOfSight) {
		    if (lineOfSight.contains(playerLocation)) {
		        return true;
		    }
		}
		return false;
	}
	
	public void updateLunaticLOS() // Updates each lunatic line of sight in all 4 directions
	{ 
		
			int maxGridRows = GameController.GUI_CONTROLLER.getGUI().getMaxRows();
			int maxGridColumns = GameController.GUI_CONTROLLER.getGUI().getMaxColumns();
			
			int row = getCurrentLocation().getRow(); int column = getCurrentLocation().getColumn(); // Current location
			
			getVerticalLineOfSight_up().clear(); getVerticalLineOfSight_down().clear();  // Clear the last vertical LOS
			getHorizontalLineOfSight_left().clear(); getHorizontalLineOfSight_right().clear(); // Clear the last horizontal LOS
			
			vertical_up:for (int i = row-1; i > 1; i--) // Up
			{ if (GridPathFinder.IsWalkable(i, column)) {  getVerticalLineOfSight_up().add(new Coordinate(i, column)); 
			} else {  break vertical_up; }	   
			
			} vertical_down:for(int i = row+1; i < maxGridRows-1; i++)  // Down
			{ if (GridPathFinder.IsWalkable(i, column)) { getVerticalLineOfSight_down().add(new Coordinate(i, column));
			} else { break vertical_down; }	
				
			} horizontal_left:for(int i = column-1; i > 0; i--) // left
			{ if (GridPathFinder.IsWalkable(row, i)) { getHorizontalLineOfSight_left().add(new Coordinate(row, i));
			} else { break horizontal_left; }
			
			} horizontal_right:for(int i = column+1; i < maxGridColumns; i++) { // right
			{ if (GridPathFinder.IsWalkable(row, i)) { getHorizontalLineOfSight_right().add(new Coordinate(row, i));
			} else { break horizontal_right; }
				
			}
		}
	}
}
