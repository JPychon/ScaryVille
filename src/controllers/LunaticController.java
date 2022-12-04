package controllers;


import models.Coordinate;
import models.Lunatic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import models.Cell;
import models.Cell.cellType;
import models.Lunatic.lunaticNextMove;
import models.Lunatic.lunaticState;
import scaryville.Main;
import models.Node;
import models.PathFinder;
import models.Coordinate.coordinateType;



public class LunaticController {

	private static int MAX_LUNATICS = 9; // Max instnaces of lunatics to create
	private static double LUNATICS_SPEED = 0.5; // Controls how often the timeline updates their movement.
	
	public static int LUNATIC_ID_INDEX = 0; // Assign an ID for every spawned lunatic (increments per new instance)
	
	private List<Lunatic> lunatics = new ArrayList<Lunatic>(); // List of lunatics chasing the player
	
	private Timeline roamTimer; // Timeline for the lunatics roam
	
	public LunaticController() { }
	
	public List<Lunatic> getLunatics() { return lunatics; }
	
	public Timeline getRoamTimerAnimation() { return roamTimer; }
	public void resumeRoamTimerAnimation() { roamTimer.playFromStart(); }
	
	public void createLunatics() { // Creates the lunatics in random-valid locations on the map

		int spawned_lunatics = MAX_LUNATICS;
		boolean lunaticSpawned = false;

		Coordinate[][] boardState = GameController.BOARD_CONTROLLER.getBoard().getMapGrid();

		// This is ugly code, any tips on how to implement it better?
		// It's meant to loop through the 2D array, pick a value, check the coordinate
		// type & spawn the lunatic if it's valid.
		// To avoid cluster, I tried iterating by +10
		// the inner loops iteration per successful new instance

		for (int row = 5; row < boardState.length - 1; row += (lunaticSpawned ? 5 : 1)) { 
			for (int col = 10; col < boardState[row].length - 1; col += (lunaticSpawned ? 10 : 1)) { 

				if (spawned_lunatics == 0)
					break;
				if (boardState[row][col].getCoordinateType() == coordinateType.BLANK) {

					Lunatic lunaticInstance = new Lunatic(); 
					
					LUNATIC_ID_INDEX++; // Increment the ID per new lunatic.
					
					lunaticInstance.setLunaticState(lunaticState.idle_roam);
					lunaticInstance.setSpawnLocation(row, col);
					lunatics.add(lunaticInstance);

					GameController.BOARD_CONTROLLER.updateBoardState(row, col, row, col, coordinateType.LUNATIC);
					GameController.GUI_CONTROLLER.updateGUIState(row, col, row, col, cellType.LUNATIC);

					spawned_lunatics--;
					lunaticSpawned = true;
					col += 5;
				} else {
					lunaticSpawned = false;
				}
			}
		}
	}

	public void respawnLunatics() {
		
		int spawned_lunatics = lunatics.size() - 1;
		boolean lunaticSpawned = false;

		Coordinate[][] boardState = GameController.BOARD_CONTROLLER.getBoard().getMapGrid();

		for (int row = 5; row < boardState.length - 1; row += (lunaticSpawned ? 10 : 1)) { 
			for (int col = 10; col < boardState[row].length - 1; col += (lunaticSpawned ? 10 : 1)) { 

				if (spawned_lunatics == 0)
					break;
				if (boardState[row][col].getCoordinateType() == coordinateType.BLANK) {

					lunatics.get(spawned_lunatics).setSpawnLocation(row, col);

					GameController.BOARD_CONTROLLER.updateBoardState(row, col, row, col, coordinateType.LUNATIC);
					GameController.GUI_CONTROLLER.updateGUIState(row, col, row, col, cellType.LUNATIC);

					spawned_lunatics--;
					lunaticSpawned = true;
					col += 5;
				} else {
					lunaticSpawned = false;
				}
			}
		}
		
		enableLunatics();
	}

	public void removeLunatics() { // Removes the lunatics from the map

		for (int lunaticsCount = 0; lunaticsCount < lunatics.size(); lunaticsCount++) {

			Coordinate currentLunaticLocation = lunatics.get(lunaticsCount).getCurrentLocation();
			int currentRow = currentLunaticLocation.getRow();
			int currentCol = currentLunaticLocation.getColumn();

			GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, 0, 0, coordinateType.BLANK); // Resets the next-cell to blank/path													
			GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, 0, 0, cellType.PATH); // Resets the next-cell to blank/path	
			
			disableLunatics(); // Disable the lunatic control/movement
		}
	}

	public void disableLunatics() { // Disables the lunatics control/movement

		for (int lunaticsCount = 0; lunaticsCount < lunatics.size(); lunaticsCount++) {
			lunatics.get(lunaticsCount).setControlStatus(true);
		}
	}
	
	public void enableLunatics() { // Disables the lunatics control/movement

		for (int lunaticsCount = 0; lunaticsCount < lunatics.size(); lunaticsCount++) {
			lunatics.get(lunaticsCount).setControlStatus(false);
		}
	}
	
	public void roamLunatics() { // Controls the lunatics movements & line of sight updates through a timeline.
		
			if(!Main.GAME_INSTANCE.isGamePaused())
			{
				roamTimer = new Timeline(new KeyFrame(Duration.seconds(LUNATICS_SPEED), e -> {
				int currentRow, currentCol, newRow, newCol;
				Coordinate currentPlayerLocation = GameController.PLAYER_CONTROLLER.getPlayerLocation();
				for (Lunatic lunatic : lunatics) {
	
						//int lunaticID = lunatic.getLunaticID();
						if(lunatic.getLunaticState() == lunaticState.idle_roam) {
						
							currentRow = lunatic.getCurrentLocation().getRow();
							currentCol = lunatic.getCurrentLocation().getColumn();
							lunaticNextMove move = lunatic.roam(); // Gets a random-move value from the enum which decides the lunatic next direction
							switch (move) {
							
							case UP:
								newRow = currentRow - 1;
								newCol = currentCol;
								if (getCellType(newRow, newCol, cellType.PATH)) {
						
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									lunatic.updateLunaticState(currentPlayerLocation);
									//printLocation(lunaticID, currentRow, currentCol, newRow, newCol, lunaticState.idle_roam);
									break;
								
								} else if (getCellType(newRow, newCol, cellType.PLAYER))  {
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									Main.GAME_INSTANCE.gameLost(); // Game over
									break;
								}
								break;
	
							case DOWN:
								newRow = currentRow + 1;
								newCol = currentCol;
								if (getCellType(newRow, newCol, cellType.PATH)) {
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									lunatic.updateLunaticState(currentPlayerLocation);
									//printLocation(lunaticID, currentRow, currentCol, newRow, newCol, lunaticState.idle_roam);
									break;
				
								} else if (getCellType(newRow, newCol, cellType.PLAYER)) 	{
							
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									Main.GAME_INSTANCE.gameLost(); // Game over
									break;
								}
								break;
	
							case LEFT:
								newRow = currentRow;
								newCol = currentCol - 1;
								if (getCellType(newRow, newCol, cellType.PATH)) {
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									lunatic.updateLunaticState(currentPlayerLocation);
									//printLocation(lunaticID, currentRow, currentCol, newRow, newCol, lunaticState.idle_roam);
									break;
									
								} else if (getCellType(newRow, newCol, cellType.PLAYER)) {
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									Main.GAME_INSTANCE.gameLost(); // Game over
									break;
								}
								break;
	
							case RIGHT:
								newRow = currentRow;
								newCol = currentCol - 1;
								if (getCellType(newRow, newCol, cellType.PATH)) {
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									lunatic.updateLunaticState(currentPlayerLocation);
									//printLocation(lunaticID, currentRow, currentCol, newRow, newCol, lunaticState.idle_roam);
									break;
									
								} else if (getCellType(newRow, newCol, cellType.PLAYER)) {
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									Main.GAME_INSTANCE.gameLost(); /// Game over
									break;
								}
								break;
							default: break;
								}
							} 
						
							if(lunatic.getLunaticState() == lunaticState.chasing) // CHASING LOGIC 
							{ 
								currentRow = lunatic.getCurrentLocation().getRow();
								currentCol = lunatic.getCurrentLocation().getColumn();
								Coordinate nextMoveToPlayer = lunatic.getNextMoveToPlayer();
								int nR = nextMoveToPlayer.getRow();
								int nC = nextMoveToPlayer.getColumn();
								lunatic.setCurrentLocation(nR, nC);
									
								if (getCellType(nR, nC, cellType.PLAYER)) {
									
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, nR, nC, coordinateType.LUNATIC_CHASING);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, nR, nC, cellType.LUNATIC_CHASING);
									Main.GAME_INSTANCE.gameLost(); // Game over
									
								} else {
								
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, nR, nC,coordinateType.LUNATIC_CHASING);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, nR, nC, cellType.LUNATIC_CHASING);
									lunatic.updateLunaticState(currentPlayerLocation);
									//printLocation(lunaticID, currentRow, currentCol, nR, nC, lunaticState.chasing);
								}
							}
							
							if(lunatic.getLunaticState() == lunaticState.investigating) { // INVESTIGATING LOGIC
								
								currentRow = lunatic.getCurrentLocation().getRow();
								currentCol = lunatic.getCurrentLocation().getColumn();
								Coordinate nextMoveToPlayer = lunatic.getNextMoveToPlayer();
								int nR = nextMoveToPlayer.getRow();
								int nC = nextMoveToPlayer.getColumn();
								lunatic.setCurrentLocation(nR, nC);
								
								if (getCellType(nR, nC, cellType.PLAYER)) {
									
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, nR, nC, coordinateType.LUNATIC_CHASING);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, nR, nC, cellType.LUNATIC_CHASING);
									Main.GAME_INSTANCE.gameLost(); // Game over
									
								} else {
									
								GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, nR, nC, coordinateType.LUNATIC_CHASING);
								GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, nR, nC,cellType.LUNATIC_CHASING);
								lunatic.updateLunaticState(currentPlayerLocation);
								//printLocation(lunaticID, currentRow, currentCol, nR, nC, lunaticState.investigating);
							}			
						}
					}
				}
			));
				
			roamTimer.setCycleCount(Animation.INDEFINITE);
			roamTimer.playFromStart();	
			
			}
		}
	
	public void printLocation(int id, int cR, int cC, int nR, int nC, lunaticState state) { // Debugging
		System.out.println("[DEBUG::LUNATIC][ID: " + id + "]- Moving up - Old: (" + cR + "," + cC  + ") - Current: (" + nR + "," + nC +") - STATE:" + state);
	}
	
	public boolean getCellType(int row, int col, cellType type) { // Returns the next cell type.
		return GameController.GUI_CONTROLLER.getGUI().getGridCellType(row - 1, col) == type;
	}
}
