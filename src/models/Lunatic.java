package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controllers.GameController;
import controllers.LunaticController;
import models.Cell.cellType;
import models.Coordinate.coordinateType;
import scaryville.Main;

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
				move = Lunatic.nextMove();
				isWalkable = PathFinder.isCellWalkable(currentRow-1, currentCol, move);
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
	
	public Coordinate getNextMoveToPlayer() { 
		if(pathToPlayer == null) {
			state = lunaticState.idle_roam;
			return currentLocation;
		} else {
			Coordinate nextPath = pathToPlayer.get(0);
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
				pathToPlayer = PathFinder.FindPath(currentLocation, currentDestination); // Find the clear path to the player location.
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
				pathToPlayer = PathFinder.FindPath(currentLocation, currentDestination); // Find the clear path to the NEW player location.
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
				pathToPlayer = PathFinder.FindPath(currentLocation, currentDestination); // Find the clear path to the NEW player location
				state = lunaticState.chasing; // Set state to chasing
			}
			
			if(!checkLOS(currentPlayerLocation)) // NO-LOS
			{
				if(pathToPlayer.size() == 0) { // If reached the last point of sighting, reset to roaming.
					state = lunaticState.idle_roam;
				}
			}
		}
	}
	public boolean checkLOS(Coordinate playerLocation) { // Checks if the player is currently in any of the 4 lines of sight.
		
		boolean los_left = false;
		boolean los_right = false;
		boolean los_up = false;
		boolean los_down = false;
		
		//System.out.println("[DEBUG::LUNATIC]- METHOD::checkLOS");
		
		for(Coordinate los : getHorizontalLineOfSight_left()) { // left
			if(los.equals(playerLocation)) {
				los_left = true;
			}
		}
		for(Coordinate los : getHorizontalLineOfSight_right()) { // right
			if(los.equals(playerLocation)) {
				//System.out.println("[DEBUG::LUNATIC::METHOD]-[checkLOS::HORIZONTAL_RIGHT]::TRUE");
				los_right = true;
			}
		}
		for(Coordinate los : getVerticalLineOfSight_up()) { // up
			if(los.equals(playerLocation)) {
				//System.out.println("[DEBUG::LUNATIC::METHOD]-[checkLOS::VERTICAL_UP]::TRUE");
				los_up = true;
			}
		}
		for(Coordinate los : getVerticalLineOfSight_down()) { // down
			if(los.equals(playerLocation)) { 
				//System.out.println("[DEBUG::LUNATIC::METHOD]-[checkLOS::VERTICAL_DOWN]::TRUE");
				los_down = true;
			}
		}
		
		if(los_up || los_down || los_left || los_right) return true;
		
		//System.out.println("[DEBUG::LUNATIC::METHOD]-[checkLOS]::FALSE");
		return false;
	}
		
	public void updateLunaticLOS() { // Updates each lunatic line of sight in all 4 directions
		
		int maxGridRows = GameController.GUI_CONTROLLER.getGUI().getMaxRows();
		int maxGridColumns = GameController.GUI_CONTROLLER.getGUI().getMaxColumns();
		
		//System.out.println("[DEBUG::LUNATIC]- METHOD::updateLunaticLOS");
		
			
			//int lunaticID = getLunaticID();
			int row = getCurrentLocation().getRow();
			int column = getCurrentLocation().getColumn();
			
			getVerticalLineOfSight_up().clear(); // Clears the LOS_UP before rechecking.
			getVerticalLineOfSight_down().clear(); // Clears the LOS_DOWN before rechecking.
			getHorizontalLineOfSight_left().clear(); // Clears the LOS_LEFT before rechecking.
			getHorizontalLineOfSight_right().clear(); // Clears the LOS_RIGHT beofre rechecking.
			
			// up
			vertical_up:for(int i = row-1; i > 1; i--) {
				
				if(GameController.GUI_CONTROLLER.getGUI().getGridCellType(i-1, column) == cellType.PATH 
						|| GameController.GUI_CONTROLLER.getGUI().getGridCellType(i-1, column) == cellType.PLAYER
						|| GameController.GUI_CONTROLLER.getGUI().getGridCellType(i-1, column) == cellType.LUNATIC) {
					
				getVerticalLineOfSight_up().add(new Coordinate(i, column)); // Adds the open coordinate to the los list
				
				//System.out.println("[DEBUG::LUNATIC][ID: " + lunaticID + "]- METHOD::updateLunaticLOS-> [VERTICAL_UP]-> [ADDING: (" + (i) + "," + column + ")");
				
				} else {
					
					//System.out.println("[DEBUG::LUNATIC][ID: " + lunaticID + "]- METHOD::updateLunaticLOS-> [VERTICAL_UP]-> [loop break]");
					break vertical_up;
				}
			}
			
			// down
			vertical_down:for(int i = row+1; i < maxGridRows-1; i++) {
				
				if(GameController.GUI_CONTROLLER.getGUI().getGridCellType(i-1, column) == cellType.PATH 
						|| GameController.GUI_CONTROLLER.getGUI().getGridCellType(i-1, column) == cellType.PLAYER
						|| GameController.GUI_CONTROLLER.getGUI().getGridCellType(i-1, column) == cellType.LUNATIC) {
					
					getVerticalLineOfSight_down().add(new Coordinate(i, column));
					
					//System.out.println("[DEBUG::LUNATIC][ID: " + lunaticID + "]- METHOD::updateLunaticLOS-> [VERTICAL_DOWN]-> [ADDING: (" + (i) + "," + column + ")");
					
				} else {
					
					//System.out.println("[DEBUG::LUNATIC][ID: " + lunaticID + "]- METHOD::updateLunaticLOS-> [VERTICAL_DOWN]-> [loop break]");
					break vertical_down;
				}
			}
			
			// left
			horizontal_left:for(int i = column-1; i > 0; i--) {
				
				if(GameController.GUI_CONTROLLER.getGUI().getGridCellType(row-1, i) == cellType.PATH 
						|| GameController.GUI_CONTROLLER.getGUI().getGridCellType(row-1, i) == cellType.PLAYER
						|| GameController.GUI_CONTROLLER.getGUI().getGridCellType(row-1, i) == cellType.LUNATIC) {
					
					getHorizontalLineOfSight_left().add(new Coordinate(row, i));
					
					//System.out.println("[DEBUG::LUNATIC][ID: " + lunaticID + "]- METHOD::updateLunaticLOS-> [HORIZONTAL_LEFT]-> [ADDING: (" + (row) + "," + i + ")");
					
				} else {
					
					//System.out.println("[DEBUG::LUNATIC][ID: " + lunaticID + "]- METHOD::updateLunaticLOS-> [HORIZONTAL_LEFT]-> [loop break]");
					break horizontal_left;
				}
			}
			
			// right
			horizontal_right:for(int i = column+1; i < maxGridColumns; i++) {
				
				if(GameController.GUI_CONTROLLER.getGUI().getGridCellType(row-1, i) == cellType.PATH 
						|| GameController.GUI_CONTROLLER.getGUI().getGridCellType(row-1, i) == cellType.PLAYER
						|| GameController.GUI_CONTROLLER.getGUI().getGridCellType(row-1, i) == cellType.LUNATIC) {
					
					getHorizontalLineOfSight_right().add(new Coordinate(row, i));
					
					//System.out.println("[DEBUG::LUNATIC][ID: " + lunaticID + "]- METHOD::updateLunaticLOS-> [HORIZONTAL_RIGHT]-> [ADDING: (" + (row) + "," + i + ")");
					
				} else {
					
					//System.out.println("[DEBUG::LUNATIC][ID: " + lunaticID + "]- METHOD::updateLunaticLOS-> [HORIZONTAL_RIGHT]-> [loop break]");
					break horizontal_right;
			}
		}
	}
}
