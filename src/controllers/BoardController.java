package controllers;

import java.util.Random;

import models.AsylumMap;
import models.Coordinate.coordinateType;

public class BoardController {

	private AsylumMap MAP_INSTANCE; // The instance of the current board type (asylumMap)
	private boolean isInitial = true; // Flag for the first time the map generates.
	
	private final int MAX_BOARD_ROWS = 30; // Max allowed rows
	private final int MAX_BOARD_COLS = 50; // Max allowed columns
	
	public BoardController(int total_rows, int total_columns, double wallsCoef) { 
		MAP_INSTANCE = new AsylumMap((total_rows > MAX_BOARD_ROWS) ? MAX_BOARD_ROWS : total_rows, (total_columns > MAX_BOARD_COLS) ? MAX_BOARD_COLS : total_columns, wallsCoef); }
	
	public AsylumMap getBoard() { return MAP_INSTANCE; } // Returns the current instance of the map
	public void resetBoardState() { this.generateBoard(); } // Resets the map by generating a new game map
	
	public void generateBoard() { // Generates the board by populating the 2D Array & coordinate types.
		
		int maxRows = MAP_INSTANCE.getMaxRows(); // Max board rows
		int maxColumns = MAP_INSTANCE.getMaxColumns(); // Max board columns
		double wallsCoefficient = MAP_INSTANCE.getMaxWalls(); // Max board walls coeff.
		
		int totalGridModules = maxRows * maxColumns; // Total number of grid-modules (row*col)
		int totalWallModules = (int)(totalGridModules * wallsCoefficient); // total number of wall-modules (row*col* walls_coeff)
		
		Random random = new Random(); // Random instance used to decide whether the next module will be a wall or not
		boolean isWall = false; // Flag to check if the module is a wall or not
		int wallCounter = 2; // used to skip loop iteration for walls to avoid clutter.
		
		for(int rowCount = 0; rowCount < maxRows; rowCount++) {
			for(int colCount = 0; colCount < maxColumns; colCount++) {
				
				if(wallCounter == 0) { // If 2 iterations have passed -> reset the random-flag value for the wall creation -> reset the counter for creating the walls.
					isWall = random.nextBoolean(); 
					wallCounter = 2; }
				
				if(isWall && totalWallModules >= 1) {
					MAP_INSTANCE.addGridCoordinate(rowCount, colCount, coordinateType.WALL);
					isWall = false;
					totalWallModules--; // Decrement the total walls needed.
				} else {
					MAP_INSTANCE.addGridCoordinate(rowCount, colCount, coordinateType.BLANK);
					wallCounter--; // Decrement the wall skip counter.
				}
			}
		}
		if(isInitial) {
			MAP_INSTANCE.insertDefaultCoordinates();
		} else {
			MAP_INSTANCE.updateStartLocation(MAP_INSTANCE.getDefaultStartLocation().getRow(), MAP_INSTANCE.getDefaultStartLocation().getColumn()); // Insert the current instance of start-location to the 2D array/
			MAP_INSTANCE.updateEndLocation(MAP_INSTANCE.getDefaultEndLocation().getRow(), MAP_INSTANCE.getDefaultEndLocation().getColumn()); // Insert the current instance of end-location to the 2D array.
		}
	}
	
	public void updateBoardState(int oldRow, int oldCol, int newRow, int newCol, coordinateType newtype) {	 // Updates the board state through coordinateType
		MAP_INSTANCE.getMapGrid()[oldRow][oldCol].setCoordinateType(coordinateType.BLANK);
		MAP_INSTANCE.getMapGrid()[newRow][newCol].setCoordinateType(newtype);
	}
}
