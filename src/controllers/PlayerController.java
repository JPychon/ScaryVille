package controllers;

import models.Cell.cellType;
import models.Coordinate;
import models.Player;
import models.Coordinate.coordinateType;
import scaryville.Main;

public class PlayerController {
	
		private Player playerInstance; // Current player instance
		private boolean playerControlStatus = false; // Disables & enables player controls [false = disabled || true = enabled]
		
		private final int PLAYER_START_ROW = 3; // Default starting row
		private final int PLAYER_START_COLUMN = 1; // Default starting column
		
		public enum playerMovement{
			LEFT,
			RIGHT,
			UP,
			DOWN,
		}

		public PlayerController() { }
		
		public boolean getPlayerControlStatus() { return playerControlStatus; } // Returns the player controls status (enabled/disabled)
		public Coordinate getPlayerLocation() { return playerInstance.getCurrentLocation(); } // Returns the current location for the player instance
		
		public void updatePlayerLocation(int row, int col) { playerInstance.setCurrentLocation(new Coordinate(row, col, coordinateType.PLAYER)); } // Updates the player current location
		
		public void updatePlayerLocation(Coordinate newLocation) {  // Updates the player current location
			if(newLocation.getCoordinateType() != coordinateType.PLAYER) { // Player instance coordinate validation
				newLocation.setCoordinateType(coordinateType.PLAYER);
			}
			playerInstance.setCurrentLocation(newLocation); 
		}
		
		public void updatePlayerLocation(playerMovement next_move) { // Updates the player current location based on the keyboard-input update from the event handler.
			
			Coordinate currentPlayerLocation = playerInstance.getCurrentLocation(); // Current location coordinate
			int currentRow = currentPlayerLocation.getRow(); // Current X location
			int currentCol = currentPlayerLocation.getColumn(); // Current Y location
			int newRow, newCol;
			
			if(!Main.GAME_INSTANCE.isGamePaused()) {
				
				switch(next_move) {
				case LEFT: // Move to left-cell
			
						newRow = currentRow;
						newCol = currentCol-1;
			
						if(getCellType(newRow, newCol, cellType.END)) {
							removePlayer(); // Remove the player instance
							GameController.GUI_CONTROLLER.resetRootNode(); // Reset the gridpane
							Main.GAME_INSTANCE.startNewGame(); // Start a new game
							
						} else if(getCellType(newRow, newCol, cellType.PATH)) {
							updatePlayerLocation(newRow, newCol);
							GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.PLAYER);
							GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.PLAYER);
							break;
							
						} else if((getCellType(newRow, newCol, cellType.LUNATIC)) || (getCellType(newRow, newCol, cellType.LUNATIC_CHASING))) {
							Main.GAME_INSTANCE.gameLost();
							
						}
						break;	
						
				case RIGHT: // Move to right-cell
					
						newRow = currentRow;
						newCol = currentCol+1;
						
						if(getCellType(newRow, newCol, cellType.END)) {
							removePlayer();
							GameController.GUI_CONTROLLER.resetRootNode(); // Reset the gridPane when the button is clicked.
							Main.GAME_INSTANCE.startNewGame();
							
						} else if(getCellType(newRow, newCol, cellType.PATH)) {
							updatePlayerLocation(newRow, newCol);
							GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.PLAYER);
							GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.PLAYER);
							break;
						
						} else if((getCellType(newRow, newCol, cellType.LUNATIC)) || (getCellType(newRow, newCol, cellType.LUNATIC_CHASING))) {
							Main.GAME_INSTANCE.gameLost();
							
						}
						break;
						
				case UP: // Move to up-cell
					
						newRow = currentRow-1;
						newCol = currentCol;
						
						if(getCellType(newRow, newCol, cellType.END)) {
							removePlayer();
							GameController.GUI_CONTROLLER.resetRootNode(); // Reset the gridPane when the button is clicked.
							Main.GAME_INSTANCE.startNewGame();
							
						} else if(getCellType(newRow, newCol, cellType.PATH)) {
							updatePlayerLocation(newRow, newCol);
							GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.PLAYER);
							GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.PLAYER);
							break;
						
						} else if((getCellType(newRow, newCol, cellType.LUNATIC)) || (getCellType(newRow, newCol, cellType.LUNATIC_CHASING))) {
							Main.GAME_INSTANCE.gameLost();
							
						}
						break;
						
				case DOWN: // Move to down-cell
					
						newRow = currentRow+1;
						newCol = currentCol;
	
						if(getCellType(newRow, newCol, cellType.END)) {
							removePlayer();
							GameController.GUI_CONTROLLER.resetRootNode(); // Reset the gridPane when the button is clicked.
							Main.GAME_INSTANCE.startNewGame();
							
						} else if(getCellType(newRow, newCol, cellType.PATH)) {
							updatePlayerLocation(newRow, newCol);
							GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.PLAYER);
							GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.PLAYER);
							break;
						
						} else if((getCellType(newRow, newCol, cellType.LUNATIC)) || (getCellType(newRow, newCol, cellType.LUNATIC_CHASING))) {
							Main.GAME_INSTANCE.gameLost();
							
						}
						break;
				}
			}
		}
		
		public boolean getCellType(int newRow, int newCol, cellType type) {
			return GameController.GUI_CONTROLLER.getGUI().getGridCellType(newRow-1, newCol) == type;
		}
		
		public void disablePlayerControls() { playerControlStatus = false; } // Disables player controls [arrows]
		public void enablePlayerControls() { playerControlStatus = true; } // Enables player controls
		
		public void createPlayer() { // Create a new instance of the player
			
			playerInstance = new Player();
			
			playerInstance.setIsAlive(true);
			playerControlStatus = true;
			
			playerInstance.setCurrentLocation(new Coordinate(PLAYER_START_ROW, PLAYER_START_COLUMN, coordinateType.PLAYER));

			GameController.BOARD_CONTROLLER.updateBoardState(PLAYER_START_ROW, PLAYER_START_COLUMN, PLAYER_START_ROW, PLAYER_START_COLUMN, coordinateType.PLAYER);
			GameController.GUI_CONTROLLER.updateGUIState(PLAYER_START_ROW, PLAYER_START_COLUMN, PLAYER_START_ROW, PLAYER_START_COLUMN, cellType.PLAYER);
		}
	
		public void removePlayer() { // Removes the instance of the player
			if(playerInstance.getIsAlive()) {
				playerInstance.setIsAlive(false);
		
				Coordinate currentPlayerLocation = playerInstance.getCurrentLocation(); // Current location coordinate
				int currentRow = currentPlayerLocation.getRow(); // Current Y location
				int currentCol = currentPlayerLocation.getColumn(); // Current X location
				
				playerInstance.setCurrentLocation(new Coordinate(0,0));
				updatePlayerLocation(1, 0);  // Resets location to 0-0 (off-grid)
				GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, 1, 0, coordinateType.BLANK); // Resets the past-cell to blank/path
				GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, 1, 0, cellType.PATH); // Resets the next-cell to blank/path
				playerControlStatus = false; // Disables the player control/input
			}
		}
	}	
