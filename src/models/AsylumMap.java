package models;

import models.Coordinate.coordinateType;

public class AsylumMap {

	
	private Coordinate[][] mapGrid; // 2D array that will hold the available-coordinates for the map.
	private Coordinate startLocation = new Coordinate(coordinateType.START); // coordinate-instance to hold the starting location
	private Coordinate endLocation  = new Coordinate(coordinateType.EXIT); // coordinate-instance to hold the ending location
	
	private int maxRows; // Max rows to created for the map-grid.
	private int maxColumns; // Max columns to be created for the map-grid
	private double wallsCoefficient;  // Max walls to be created for the map-grid [not including borders][coefficient double, ex: 0.25]
	
	private final int START_LOCATION_ROW = 1; // ROW 1 (gridpane)
	private final int START_LOCATION_COL = 1; // COLUMN 1 (gridpane)
	
	private final int END_LOCATION_ROW = maxRows;
	private final int END_LOCATION_COL = maxColumns;
	
	public AsylumMap(int total_rows, int total_columns, double WallsCoef) {
		
		maxRows = total_rows;
		maxColumns = total_columns;
		wallsCoefficient = WallsCoef;
		
		setMapGrid(total_rows, total_columns);
		setStartLocation(START_LOCATION_ROW, START_LOCATION_COL);
		setEndLocation(END_LOCATION_ROW, END_LOCATION_COL);
	}
	
	public void setMapGrid(int rows, int columns) { mapGrid = new Coordinate[maxRows][maxColumns]; } // Initalizes a new instance of the mapGrid 2D Array
	public void setMaxRows(int rows) { maxRows = rows; }
	public void setMaxColumns(int columns) { maxColumns = columns; }
	public void setMaxWalls(double walls) { wallsCoefficient = walls; }
	
	public Coordinate[][] getMapGrid() { return mapGrid; }
	public Coordinate getStartLocation() { return startLocation; }
	public Coordinate getEndLocation() { return endLocation; }
	public int getMaxRows() { return maxRows; }
	public int getMaxColumns() { return maxColumns; }
	public double getMaxWalls() { return wallsCoefficient; }
	
	public Coordinate getDefaultStartLocation() { return new Coordinate(START_LOCATION_ROW, START_LOCATION_COL, coordinateType.START); }
	public Coordinate getDefaultEndLocation() { return new Coordinate(END_LOCATION_ROW, END_LOCATION_COL, coordinateType.EXIT); }

	public coordinateType getBoardCoordinateType(int row, int col) { return mapGrid[row][col].getCoordinateType(); }
	
	public void addGridCoordinate(int row, int column, coordinateType type) { mapGrid[row][column] = new Coordinate(row, column, type); } // Adds a new coordinate instance to the 2D array.
	public void updateGridCoordinate(Coordinate coordinate) {  mapGrid[coordinate.getRow()][coordinate.getColumn()] = coordinate; } // Replaces the coordiante instead in the 2D array (coordiante parameter)
	
	public void insertDefaultCoordinates() { // Used only in the initial map generation as the coordinates aren't set yet.
		mapGrid[START_LOCATION_ROW][START_LOCATION_COL] = startLocation;
		mapGrid[END_LOCATION_ROW][END_LOCATION_COL] = endLocation;
	}
	
	public void setStartLocation(int row, int column) { // sets the X & Y values of the start-coordinate instance
		startLocation.setRow(row);
		startLocation.setColumn(column);
		
	}
	public void setEndLocation(int row, int column) { // sets the X & Y values of the end-coordinate instance
		endLocation.setRow(row);
		endLocation.setColumn(column); 
	}
	
	public void updateStartLocation(int row, int column) { // Updates the start-location instance by resetting the old-instance in the 2D array & creating a new one in the 2D array
		addGridCoordinate(startLocation.getRow(), startLocation.getColumn(), coordinateType.BLANK);
		setStartLocation(row, column); 
		mapGrid[row][column] = startLocation;
	}
	
	public void updateEndLocation(int row, int column) { // Updates the end-location instance by restting the old-instance in the 2D array & creating a new one in the 2D array.
		addGridCoordinate(endLocation.getRow(), endLocation.getColumn(), coordinateType.BLANK);
		endLocation = new Coordinate(row, column, coordinateType.EXIT);
		mapGrid[row][column] = endLocation;
	}
}

