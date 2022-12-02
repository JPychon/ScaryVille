package controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Cell;
import models.Cell.cellType;
import models.Coordinate;
import models.Coordinate.coordinateType;
import models.MapPane;
import models.Tulpe4;

public class GUIController  {
	
	private MapPane GUI_INSTANCE;
	private Stage primary_stage;
	
	public GUIController(Stage mainStage, int rowSize, int colSize) 
	{ 
		primary_stage = mainStage;
		GUI_INSTANCE = new MapPane(rowSize, colSize); 
	}
		
	public MapPane getGUI() { return GUI_INSTANCE; }
		
	public void resetRootNode() 
	{ 
		GameController.BOARD_CONTROLLER.resetBoardState(); // Resets the board state
		populateGrid(false); // Poplates the grid with the new state of cells/labels
	}
	
	public void initalizeMainStage() {
		
		primary_stage.setTitle("ScaryVille");
		primary_stage.setResizable(false);
		
		Image mainIcon = new Image("icon.png"); // Application Icon
		primary_stage.getIcons().add(mainIcon);
		
		primary_stage.setScene(GUI_INSTANCE.getScene());
		primary_stage.show();
	}
	
	public Tulpe4<List<Coordinate>, List<Coordinate>, List<Coordinate>, List<Coordinate>> getClearCellsFromCoordinate(int row, int column) 
	{
		Coordinate[][] gridState = GameController.BOARD_CONTROLLER.getBoard().getMapGrid();
		int maxGridRows = GUI_INSTANCE.getMaxRows();
		int maxGridColumns = GUI_INSTANCE.getMaxColumns();
		
		List<Coordinate> up_LOS = new ArrayList<Coordinate>();
		List<Coordinate> down_LOS = new ArrayList<Coordinate>();
		List<Coordinate> left_LOS = new ArrayList<Coordinate>();
		List<Coordinate> right_LOS = new ArrayList<Coordinate>();
		
		for(int up = row; up >= 2; up--) {
			if(gridState[up][column].getCoordinateType() != coordinateType.WALL) {
				up_LOS.add(gridState[up][column]);
			} else {
				break;
			}
		}
		
		for(int down = row; down < maxGridRows-1; down++) {
			if(gridState[down][column].getCoordinateType() != coordinateType.WALL) {
				down_LOS.add(gridState[down][column]);
			} else {
				break;
			}
		}
		
		for(int left = column; left >= 1; left--) {
			if(gridState[row][left].getCoordinateType() != coordinateType.WALL) {
				left_LOS.add(gridState[row][left]);
			} else {
				break;
			}
		}
		
		for(int right = column; right < maxGridColumns-1; right++) {
			if(gridState[row][right].getCoordinateType() != coordinateType.WALL) {
				right_LOS.add(gridState[row][right]);
			} else {
				break;
			}
		}
		
		return new Tulpe4<>(up_LOS, down_LOS, left_LOS, right_LOS); 
	}
	
	public void populateGrid(boolean isInitial) { 
		
		Coordinate[][] gridState = GameController.BOARD_CONTROLLER.getBoard().getMapGrid();
		int maxGridRows = GUI_INSTANCE.getMaxRows();
		int maxGridColumns = GUI_INSTANCE.getMaxColumns();
		

		for(int rowCounter = 1; rowCounter < maxGridRows; rowCounter++) { // skip row 0 for the hbox node
			for(int colCounter = 0; colCounter < maxGridColumns; colCounter++) {
				
					Cell gridCell;
					
					if((rowCounter == 1 && colCounter == 0) || (rowCounter == 1 && colCounter == maxGridColumns-1)) { // TOP LEFT/RIGHT CORNER
						
						gridCell = new Cell(cellType.TOP_CORNER); // CORNER-WALL
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
						
					} else if ((rowCounter == maxGridRows-1 && colCounter == maxGridColumns-1) || (rowCounter == maxGridRows-1 && colCounter == 0)){
						
						gridCell = new Cell(cellType.BOTTOM_CORNER); // CORNER-WALL
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
						
					} else if(rowCounter == 1) {
		
						gridCell = new Cell(cellType.TOP_BORDER); // TOP-BORDER
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
						
					} else if (rowCounter == maxGridRows-1) {
						
						gridCell = new Cell(cellType.BOTTOM_BORDER); // BOTTOM-BORDER
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
						
					} else if (colCounter == 0 && rowCounter > 1 && rowCounter < maxGridRows-1) {
						
						gridCell = new Cell(cellType.LEFT_BORDER); // LEFT-BORDER
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
						
					} else if (colCounter == maxGridColumns-1 && rowCounter > 1 && rowCounter < maxGridRows-1) { 
						
						gridCell = new Cell(cellType.RIGHT_BORDER); // RIGHT-BORDER
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
						
					} else if (gridState[rowCounter-1][colCounter].getCoordinateType() == coordinateType.WALL) { 
						
						gridCell = new Cell(cellType.WALL); // REGULAR-WALL
						
					} else {
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.BLANK);
						gridCell = new Cell(cellType.PATH); // PATHWAY
					}
					
					if(isInitial) { // Initial map generation
						GUI_INSTANCE.insertCellToArray(gridCell, rowCounter-1, colCounter);
						GUI_INSTANCE.getRootNode().add(gridCell.getNode(), colCounter, rowCounter);
						
					} else {
						GUI_INSTANCE.getRootNode().getChildren().remove(GUI_INSTANCE.getCellFromArray(rowCounter-1, colCounter).getNode()); //  Remove the old label from the cell
						GUI_INSTANCE.insertCellToArray(gridCell, rowCounter-1, colCounter);
						GUI_INSTANCE.getRootNode().add(gridCell.getNode(), colCounter, rowCounter); // Add the label-node of the cell to the gridpane.
					}		
				}
			}
		
			Cell startCell = new Cell(cellType.START); // Start cell
			Cell endCell = new Cell(cellType.END); // End cell
			
			GUI_INSTANCE.insertCellToArray(startCell, 1, 1);
			GUI_INSTANCE.getRootNode().add(startCell.getNode(), 1, 2);
			gridState[1][1].setCoordinateType(coordinateType.START);
			
			GUI_INSTANCE.insertCellToArray(endCell, maxGridRows-3, maxGridColumns-2);
			GUI_INSTANCE.getRootNode().add(endCell.getNode(), maxGridColumns-2, maxGridRows-2);
			gridState[maxGridRows-3][maxGridColumns-2].setCoordinateType(coordinateType.EXIT);
		}
	
	
	public void updateGUIState(int oldRow, int oldCol, int newRow, int newCol, coordinateType newType) {
		
		Cell oldPlayerCell = new Cell(cellType.PATH); // Cell holding the player's last position
		
		GUI_INSTANCE.getRootNode().getChildren().remove(GUI_INSTANCE.getCellFromArray(oldRow-1, oldCol).getNode()); // Remove the old cell from the gridPane
		GUI_INSTANCE.insertCellToArray(oldPlayerCell, oldRow-1, oldCol); // Update the 2D Cell Array
		GUI_INSTANCE.getRootNode().add(oldPlayerCell.getNode(), oldCol, oldRow);  // Add the new cell (BLANK)
		
		if(newType == coordinateType.PLAYER) {
			
			Cell newPlayerCell = new Cell(cellType.PLAYER); // Cell holding the player's current position
			
			GUI_INSTANCE.getRootNode().getChildren().remove(GUI_INSTANCE.getCellFromArray(newRow-1, newCol).getNode()); // Remove the old cell from the gridPane
			GUI_INSTANCE.insertCellToArray(newPlayerCell, newRow-1, newCol);
			GUI_INSTANCE.getRootNode().add(newPlayerCell.getNode(), newCol, newRow); // Add the new cell (Player)
			
		} else if(newType == coordinateType.LUNATIC) {
			
			Cell newLunaticCell = new Cell(cellType.LUNATIC);
			
			GUI_INSTANCE.getRootNode().getChildren().remove(GUI_INSTANCE.getCellFromArray(newRow-1, newCol).getNode()); // Remove the old cell from the gridpane
			GUI_INSTANCE.insertCellToArray(newLunaticCell, newRow-1, newCol);
			GUI_INSTANCE.getRootNode().add(newLunaticCell.getNode(), newCol, newRow); // Add the new cell (Lunatic)
		}
	}
}



