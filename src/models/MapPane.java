package models;

import controllers.GUIController;
import controllers.InputController;
import controllers.MouseController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import models.Cell.cellType;

public class MapPane {

	private GridPane rootNode = new GridPane();
	private Scene mainWindow = new Scene(rootNode);
	
	private HBox toolBar = new HBox(); // HBox for the menu/tool-bar

	private Cell[][] gridCells; // 2D Array to hold the cells in the grid.
	
	private String stylePath_labels = GUIController.class.getResource("grid_labels.css").toExternalForm(); // Path to the grid-labels style file.
	private String stylePath_toolbar = GUIController.class.getResource("hbox_toolbar.css").toExternalForm(); // Path to the hbox-toolbar style file.
	private String stylePath_gridPane = GUIController.class.getResource("grid.css").toExternalForm(); // Path to the gridpane style file.
	
	private int maxGridRows; // Max rows for the GridPane.
	private int maxGridColumns; // Max columns for the GridPane
	
	public MapPane(int rowSize, int colSize) {
		
		maxGridRows = rowSize;
		maxGridColumns = colSize;
		gridCells = new Cell[rowSize-1][colSize];
		
		initalizeRootNode();
		initalizeHBoxMenu();
	}
	
	@SuppressWarnings("unused")
	private void initalizeScene() { }
	public Scene getScene() { return mainWindow; }
	
	public void insertCellToArray(Cell cell, int rowNum, int colNum) { gridCells[rowNum][colNum] = cell; }
	public Cell getCellFromArray(int rowNum, int colNum) { return gridCells[rowNum][colNum]; }
	public cellType getGridCellType(int row, int col) { return gridCells[row][col].getCellType(); }
	
	public int getMaxRows() { return maxGridRows; }
	public int getMaxColumns() { return maxGridColumns; }
	public GridPane getRootNode() { return rootNode; }
	
	public void setMaxRows(int rows) { maxGridRows = rows; }
	public void setMaxColumns(int cols) { maxGridColumns = cols; }
		
	private void initalizeRootNode() {
		
		rootNode.setOnKeyReleased(new InputController()); // Attach an EventHandler for key-presses to the gridPane (onKeyPressed didn't work for some reason)
		
		rootNode.getStylesheets().add(stylePath_labels); // Reference the grid-labels style sheet
		rootNode.getStylesheets().add(stylePath_toolbar); // Reference the hbox-toolbar style sheet
		rootNode.getStylesheets().add(stylePath_gridPane); // Reference the gridpane style sheet
		
		rootNode.getStyleClass().add("gridpane");
	}
	
	private void initalizeHBoxMenu() {
		
		Region leftFill = new Region(); // left-filler region for spacing
		Region rightFill = new Region(); // right-filler region for spacing
		
		Button newGameButton = new Button("New Game"); // Button to reset the game level
		newGameButton.setId("hbox-new-game-button");
		HBox.setMargin(newGameButton, new Insets(0, 10, 0, 0)); // Add margins to the right
		
		Effect shadow = new InnerShadow(); 
		Effect glow = new Glow(0.5);
		
		newGameButton.setOnMousePressed(new MouseController()); // Attach an event listener
		newGameButton.setOnMouseEntered(e -> {
			newGameButton.setEffect(shadow);
			newGameButton.setEffect(glow);
		}); // Inner shadow & glow when the mouse hovers
		
		newGameButton.setOnMouseExited(e -> newGameButton.setEffect(null)); // Remove shadow when mouse stops hovering
		
		DropShadow titleShadow = new DropShadow();
		titleShadow.setColor(new Color(0.6447, 0.1164, 0.1164, 1.0));
		titleShadow.setOffsetY(0.5f);
		
		Label titleLabel = new Label(); // title-label'		
		titleLabel.setId("hbox-title-label");
		titleLabel.setText("ASYLUM OF SCARYVILLE");
		titleLabel.setEffect(titleShadow);
		
		HBox.setMargin(titleLabel, new Insets(0, 0, 0, 0));
		HBox.setHgrow(leftFill, Priority.ALWAYS);
		HBox.setHgrow(rightFill, Priority.ALWAYS);
		
		toolBar.getStyleClass().add("hbox-toolbar");
		toolBar.setAlignment(Pos.CENTER);
		toolBar.getChildren().addAll(leftFill, titleLabel, rightFill, newGameButton);
		rootNode.add(toolBar, 0, 0, maxGridColumns, 1); // Add the hbox to the rootnode spanning row 0 & the columns.
	}
}
