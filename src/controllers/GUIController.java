package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Cell;
import models.Cell.cellType;
import models.Coordinate;
import models.Coordinate.coordinateType;
import models.MapPane;

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
	
	public void hideGrid(boolean isPause, boolean isLost, boolean isWin) { // Hides the grid by attaching a timeline to all the labels & changing their opacity/size to 0
		
		int maxGridRows = GUI_INSTANCE.getMaxRows();
		int maxGridColumns = GUI_INSTANCE.getMaxColumns();
		GridPane root = GUI_INSTANCE.getRootNode();
	
		if(isLost) { root.setStyle("-fx-background-image: url('game-lost.png')"); }
		if(isPause) { root.setStyle("-fx-background-image: url('game-pause.png')"); }
		if(isWin) { root.setStyle("-fx-background-image: url('win-background.png')"); }
	
		for(int rowCounter = 1; rowCounter < maxGridRows; rowCounter++) { 
			for(int colCounter = 0; colCounter < maxGridColumns; colCounter++) {
				
			Label label = (Label) GUI_INSTANCE.getCellFromArray(rowCounter-1, colCounter).getNode();
				
			Timeline hide = new Timeline(new KeyFrame(Duration.seconds(1),new KeyValue(label.opacityProperty(), 0),  // Fadeout animation
			new KeyValue(label.maxWidthProperty(),  0),new KeyValue(label.maxHeightProperty(), 0)));
			hide.play();
			}
		}
	}
	
	public void hideGrid_instant() { // Hides the grid instantly
		
		int maxGridRows = GUI_INSTANCE.getMaxRows();
		int maxGridColumns = GUI_INSTANCE.getMaxColumns();
	
		for(int rowCounter = 1; rowCounter < maxGridRows; rowCounter++) { 
			for(int colCounter = 0; colCounter < maxGridColumns; colCounter++) {
				
			Label label = (Label) GUI_INSTANCE.getCellFromArray(rowCounter-1, colCounter).getNode();
			
			Timeline hide = new Timeline(new KeyFrame(Duration.seconds(0.2),new KeyValue(label.opacityProperty(), 0),  // Fadeout animation
			new KeyValue(label.maxWidthProperty(),  0),new KeyValue(label.maxHeightProperty(), 0)));
			hide.play();
			
			}
		}
	}
	
	public void unhideGrid() { // Shows the grid by attaching a timelime to all the labels & changing their opacity/size to 1
		
		int maxGridRows = GUI_INSTANCE.getMaxRows();
		int maxGridColumns = GUI_INSTANCE.getMaxColumns();
	
		for(int rowCounter = 1; rowCounter < maxGridRows; rowCounter++) { 
			for(int colCounter = 0; colCounter < maxGridColumns; colCounter++) {
				
			Label label = (Label) GUI_INSTANCE.getCellFromArray(rowCounter-1, colCounter).getNode();
		    double standardWidth  = label.getWidth();
		    double standardHeight = label.getHeight();
		    label.setMaxSize(standardWidth, standardHeight);
		    
			Timeline show = new Timeline(new KeyFrame(Duration.seconds(1),new KeyValue(label.opacityProperty(), 1), // Fadein animation
			new KeyValue(label.maxWidthProperty(),  standardWidth),new KeyValue(label.maxHeightProperty(), standardHeight)));
			show.play();
			
			}
		}
	}
	
	public void populateGrid(boolean isInitial) { 
		
		Coordinate[][] gridState = GameController.BOARD_CONTROLLER.getBoard().getMapGrid();
		int maxGridRows = GUI_INSTANCE.getMaxRows();
		int maxGridColumns = GUI_INSTANCE.getMaxColumns();
	
		for(int rowCounter = 1; rowCounter < maxGridRows; rowCounter++) { // skip row 0 for the hbox node
			for(int colCounter = 0; colCounter < maxGridColumns; colCounter++) {
				
					Cell gridCell;
					if(rowCounter == 2 && colCounter == 1) // Start cell
					{
						gridCell = new Cell(cellType.START);
						if(isInitial) 
						{
							GUI_INSTANCE.insertCellToArray(gridCell, 1, 1);
							GUI_INSTANCE.getRootNode().add(gridCell.getNode(), 1, 2);
							gridState[1][1].setCoordinateType(coordinateType.START);
							continue;
						} 
						else 
						{
							GUI_INSTANCE.getRootNode().getChildren().remove(GUI_INSTANCE.getCellFromArray(1, 1).getNode());
							GUI_INSTANCE.insertCellToArray(gridCell, 1, 1);
							GUI_INSTANCE.getRootNode().add(gridCell.getNode(), 1, 2);
							gridState[1][1].setCoordinateType(coordinateType.START);
							continue;
							
						}
					}
					else if(rowCounter == maxGridRows-2 && colCounter == maxGridColumns-2) // End cell
					{
						gridCell = new Cell(cellType.END);
						if(isInitial) 
						{
							GUI_INSTANCE.insertCellToArray(gridCell, maxGridRows-3, maxGridColumns-2);
							GUI_INSTANCE.getRootNode().add(gridCell.getNode(), maxGridColumns-2, maxGridRows-2);
							gridState[maxGridRows-3][maxGridColumns-2].setCoordinateType(coordinateType.EXIT);
							continue;
						}
						else 
						{
							GUI_INSTANCE.getRootNode().getChildren().remove(GUI_INSTANCE.getCellFromArray(maxGridRows-3, maxGridColumns-2).getNode());
							GUI_INSTANCE.insertCellToArray(gridCell, maxGridRows-3, maxGridColumns-2);
							GUI_INSTANCE.getRootNode().add(gridCell.getNode(), maxGridColumns-2, maxGridRows-2);
							gridState[maxGridRows-3][maxGridColumns-2].setCoordinateType(coordinateType.EXIT);
							continue;
						}
					}
					else if((rowCounter == 1 && colCounter == 0) || (rowCounter == 1 && colCounter == maxGridColumns-1)) // TOP LEFT/RIGHT CORNER
					{ 
						gridCell = new Cell(cellType.TOP_CORNER); // CORNER-WALL
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
					} 
					else if ((rowCounter == maxGridRows-1 && colCounter == maxGridColumns-1) || (rowCounter == maxGridRows-1 && colCounter == 0))
					{
						
						gridCell = new Cell(cellType.BOTTOM_CORNER); // CORNER-WALL
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
					} 
					else if(rowCounter == 1) 
					{
						gridCell = new Cell(cellType.TOP_BORDER); // TOP-BORDER
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
					}
					else if (rowCounter == maxGridRows-1) 
					{
						gridCell = new Cell(cellType.BOTTOM_BORDER); // BOTTOM-BORDER
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
					} 
					else if (colCounter == 0 && rowCounter > 1 && rowCounter < maxGridRows-1) 
					{
						gridCell = new Cell(cellType.LEFT_BORDER); // LEFT-BORDER
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
					}
					else if (colCounter == maxGridColumns-1 && rowCounter > 1 && rowCounter < maxGridRows-1) 
					{ 
						gridCell = new Cell(cellType.RIGHT_BORDER); // RIGHT-BORDER
						gridState[rowCounter-1][colCounter].setCoordinateType(coordinateType.WALL);
					} 
					else if (gridState[rowCounter-1][colCounter].getCoordinateType() == coordinateType.WALL) 
					{ 
						gridCell = new Cell(cellType.WALL); // REGULAR-WALL
					} 
					else 
					{
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
		}
	
	public void updateGUIState(int oldRow, int oldCol, int newRow, int newCol, cellType newType) {
		
		Cell oldCell = new Cell(cellType.PATH); // Cell holding the player's last position
		
		GUI_INSTANCE.getRootNode().getChildren().remove(GUI_INSTANCE.getCellFromArray(oldRow-1, oldCol).getNode()); // Remove the old cell from the gridPane
		GUI_INSTANCE.insertCellToArray(oldCell, oldRow-1, oldCol); // Update the 2D Cell Array
		GUI_INSTANCE.getRootNode().add(oldCell.getNode(), oldCol, oldRow);  // Add the new cell (BLANK)
		
		Cell newCell = new Cell(newType); // Cell holding the player's current position
			
		GUI_INSTANCE.getRootNode().getChildren().remove(GUI_INSTANCE.getCellFromArray(newRow-1, newCol).getNode()); // Remove the old cell from the gridPane
		GUI_INSTANCE.insertCellToArray(newCell, newRow-1, newCol);
		GUI_INSTANCE.getRootNode().add(newCell.getNode(), newCol, newRow); // Add the new cell (Player)
	}
}



