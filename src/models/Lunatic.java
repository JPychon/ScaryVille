package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.Coordinate.coordinateType;

public class Lunatic {

	private Coordinate spawnLocation = new Coordinate(coordinateType.LUNATIC); // Starting point
	private Coordinate currentLocation;
	
	private boolean isFrozen = true; // Disables or enables movement/control
	
	private List<List<Coordinate>> line_of_sight = new ArrayList<List<Coordinate>>(); // 2D list for both vertical & horizontal line of sight
	
	private List<Coordinate> left_horizontal_line_of_sight = new ArrayList<Coordinate>(); // List of columns visible left of the lunatic
	private List<Coordinate> right_horizontal_line_of_sight = new ArrayList<Coordinate>(); // List of columns visible right of the lunatic
	
	private List<Coordinate> up_vertical_line_of_sight = new ArrayList<Coordinate>(); // List of rows visible over the lunatic
	private List<Coordinate> down_vertical_line_of_sight = new ArrayList<Coordinate>(); // List of rows visible under the lunatic
	
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
			isFrozen = false;
			currentLocation = spawnLocation;
		}
	
	private static final Random randomDirection = new Random(); // Random instance for picking the next direction
	
	private static lunaticNextMove nextMove() { // Static method to pick the next move.
		lunaticNextMove[] moves = lunaticNextMove.values(); // Creates an array of the enum values
		return moves[randomDirection.nextInt(moves.length-1)]; // Picks a random value based on the enum-length & returns it.
	}
	
	public lunaticNextMove roam() { if(!isFrozen) { return nextMove(); } else { return lunaticNextMove.IDLE; } }
	
	public Coordinate getSpawnLocation() { return spawnLocation; }
	public boolean getControlStatus() { return isFrozen; }
	public List<List<Coordinate>> getLineOfSight() { return line_of_sight; }
	public Coordinate getCurrentLocation() { return currentLocation; }
	
	public List<Coordinate> getHorizontalLineOfSight_left() { return left_horizontal_line_of_sight;}
	public List<Coordinate> getHorizontalLineOfSight_right() { return right_horizontal_line_of_sight;}
	
	public List<Coordinate> getVerticalLineOfSight_up() { return up_vertical_line_of_sight; }
	public List<Coordinate> getVerticalLineOfSight_down() { return down_vertical_line_of_sight; }
	
	public lunaticState getLunaticState() { return state; }
	
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
}
