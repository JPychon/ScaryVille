package models;

public class Coordinate { // Tracks the grid-location for the board (logic)
	
	private int row; // Y value of the coordinate.
	private int column; // X value of the coordinate.
	
	private char coordinateValue; // Holds the type of coordinate in a char.
	private coordinateType coordinateType; // Holds the enum-value of the coordinate.

	public enum coordinateType { // Different types of coordinates.
		WALL, // Normal wall 						[CHAR: 'W']
		BLANK, // Empty path 						[CHAR: 'B']
		START, // Start point 						[CHAR: 'S']
		EXIT, // End point 							[CHAR: 'E']
		PLAYER, // Player point						[CHAR: 'P']
		LUNATIC, // Lunatic point					[CHAR: 'L']
		LUNATIC_CHASING, // Lunatic Chasing			[CHAR: 'C']
	}
	
	public int getRow() { return this.row; } // returns Y value of the coordinate.
	public int getColumn() { return this.column; } // returns X value of the coordinate.
	public char getCoordinateValue() { return this.coordinateValue; } // returns the type of coordinate in a char.
	public coordinateType getCoordinateType() { return this.coordinateType; } // returns the type of coordinate enum value.
	 
	public void setRow(int row) { this.row = row; } // sets the Y value of the coordinate.
	public void setColumn(int column) { this.column = column; } // sets the X value of the coordinate.
	public void setCoordinateValue(char value) { this.coordinateValue = value; } // sets the type of coordinate through the char value.
	public void setCoordinateType(coordinateType type) { this.coordinateType = type; } // sets the type of coordinate through the enum.
	
	public Coordinate() { } // Default consturctor
	
	public Coordinate(int row, int column) { // Constructor for a typeless coordinate (x,y values only)
		
		this.row = row;
		this.column = column;
	}
	
	public Coordinate(coordinateType type) {  // Constructor for a locationless coordinate.
		
		coordinateType = type; 
		switch(type) {
		
		case WALL:
			coordinateValue = 'W'; // Normal wall
			break;
		case BLANK:
			coordinateValue = 'B'; // Empty path
			break;
		case START:
			coordinateValue = 'S';  // Start point
			break;
		case EXIT:
			coordinateValue = 'E'; // End point
			break;
		case PLAYER:
			coordinateValue = 'P'; // Player point
			break;
		case LUNATIC:
			coordinateValue = 'L'; // Luantic point
			break;
		case LUNATIC_CHASING:
			coordinateValue = 'C'; // Lunatic chasing point
			break;
		}
	}
	
	public Coordinate(int row, int column, coordinateType coordinateType) { // Constructor for a coordinate with a type
		
		this.row = row;
		this.column = column;
		this.coordinateType = coordinateType;
		
		switch(coordinateType) {
		
		case WALL:
			coordinateValue = 'W'; // Normal wall
			break;
		case BLANK:
			coordinateValue = 'B'; // Empty path
			break;
		case START:
			coordinateValue = 'S';  // Start point
			break;
		case EXIT:
			coordinateValue = 'E'; // End point
			break;
		case PLAYER:
			coordinateValue = 'P'; // Player point
			break;
		case LUNATIC:
			coordinateValue = 'L'; // Luantic point
			break;
		case LUNATIC_CHASING:
			coordinateValue = 'C'; // Lunatic chasing point
			break;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		Coordinate coordinate = (Coordinate) o;
		return row == coordinate.row && column == coordinate.column;
	}
	
	@Override
	public String toString() { return "(" + this.row + "," + this.column + ")"; }
}
