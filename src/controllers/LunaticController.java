package controllers;

import models.Coordinate;
import models.Lunatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import models.Cell.cellType;
import models.Lunatic.lunaticNextMove;
import models.Lunatic.lunaticState;
import scaryville.Main;
import models.Coordinate.coordinateType;
import models.GridPathFinder;

public class LunaticController {

	private static int MAX_LUNATICS = 15; // Max instnaces of lunatics to create
	private static double LUNATICS_SPEED = 0.4; // Controls how often the timeline updates their movement.
	
	public static int LUNATIC_ID_INDEX = 0; // Assign an ID for every spawned lunatic (increments per new instance)
	
	private List<Lunatic> lunatics = new ArrayList<Lunatic>(); // List of lunatics chasing the player
	
	private Timeline roamTimer; // Timeline for the lunatics roam
	
	public LunaticController() { }
	
	public List<Lunatic> getLunatics() { return lunatics; }
	
	public Timeline getRoamTimerAnimation() { return roamTimer; }
	public void resumeRoamTimerAnimation() { roamTimer.playFromStart(); }

	public void createLunatics()
	{
		int maxRows = GameController.BOARD_CONTROLLER.getBoard().getMaxRows();
		int maxColumns = GameController.BOARD_CONTROLLER.getBoard().getMaxColumns();
		
		Random randomSpawn = new Random(); // Random instance for the spawn location of each lunatic
		boolean hasSpawned = false; // Flag whether the lunatic instance has been created or not
		
		for (int i = 0; i < MAX_LUNATICS; i++) 
		{
			hasSpawned = false;
			while(!hasSpawned) // Keeps running the code until a lunatic has been spawned in an open cell
			{
				int spawnRow = randomSpawn.nextInt((maxRows-2 - 5) +1) + 5; // Random value for the row (5-max range)
				int spawnCol = randomSpawn.nextInt((maxColumns-1 - 5) +1) + 5; // Random value for the column (5-max range)
				if(GridPathFinder.IsWalkable(spawnRow, spawnCol)) // Check if this value is valid 
				{
					Lunatic lunaticInstance = new Lunatic();

					lunaticInstance.setLunaticState(lunaticState.idle_roam);
					lunaticInstance.setSpawnLocation(spawnRow, spawnCol);
					lunatics.add(lunaticInstance);
					
					LUNATIC_ID_INDEX++; // Increment the ID per new lunatic.
					
					GameController.BOARD_CONTROLLER.updateBoardState(spawnRow, spawnCol, spawnRow, spawnCol, coordinateType.LUNATIC);
					GameController.GUI_CONTROLLER.updateGUIState(spawnRow, spawnCol, spawnRow, spawnCol, cellType.LUNATIC);
					hasSpawned = true;
				}
			}
		}
	}
	public void removeLunatics() { // Removes the lunatics from the map

		for (Lunatic lunatic : lunatics) {

			Coordinate currentLunaticLocation = lunatic.getCurrentLocation();
			int currentRow = currentLunaticLocation.getRow();
			int currentCol = currentLunaticLocation.getColumn();
			
			GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, currentRow, currentCol, coordinateType.BLANK); // Resets the next-cell to blank/path													
			GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, currentRow, currentRow, cellType.PATH); // Resets the next-cell to blank/path	
		}
		
		lunatics.clear();
		LUNATIC_ID_INDEX = 0;
	}

	public void disableLunatics() { for (Lunatic lunatic : lunatics) { lunatic.setControlStatus(true); roamTimer.stop(); } } // Disables the lunatics movement/control
	public void enableLunatics() { for (Lunatic lunatic : lunatics) { lunatic.setControlStatus(false); roamTimer.playFromStart();} } // Enables the lunatic movement/control

	public void roamLunatics() { // Controls the lunatics movements & line of sight updates through a timeline.
		
			if(GameController.isPaused == false && GameController.isLost == false)
			{
				roamTimer = new Timeline(new KeyFrame(Duration.seconds(LUNATICS_SPEED), e -> {
				int currentRow, currentCol, newRow, newCol;
				Coordinate currentPlayerLocation = GameController.PLAYER_CONTROLLER.getPlayerLocation();
				mainLoop:for (Lunatic lunatic : lunatics) {
	
						if(lunatic.getLunaticState() == lunaticState.idle_roam) {
						
							currentRow = lunatic.getCurrentLocation().getRow();
							currentCol = lunatic.getCurrentLocation().getColumn();
							lunaticNextMove move = lunatic.roam(currentRow, currentCol); // Gets a random-move value from the enum which decides the lunatic next direction
							switch (move) {
							
							case UP:
								newRow = currentRow - 1;
								newCol = currentCol;
								if (getCellType(newRow, newCol, cellType.PATH)) {
						
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									lunatic.updateLunaticState(currentPlayerLocation);
								}
								if (getCellType(newRow, newCol, cellType.PLAYER))  { // Catches the player
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									removeLunatics();
									Main.GAME_INSTANCE.gameLost(); // Game over
									break mainLoop;
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
									break;
								}
								if (getCellType(newRow, newCol, cellType.PLAYER)) 	{ // Catches the player
							
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									removeLunatics();
									Main.GAME_INSTANCE.gameLost(); // Game over
									break mainLoop;
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
									break;
								}
								if (getCellType(newRow, newCol, cellType.PLAYER)) { // Catches the player
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									removeLunatics();
									Main.GAME_INSTANCE.gameLost(); // Game over
									break mainLoop;
								}
								break;
	
							case RIGHT:
								newRow = currentRow;
								newCol = currentCol + 1;
								if (getCellType(newRow, newCol, cellType.PATH)) {
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									lunatic.updateLunaticState(currentPlayerLocation);
								}
								if (getCellType(newRow, newCol, cellType.PLAYER)) { // Catches the player
								
									lunatic.setCurrentLocation(newRow, newCol);
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol, coordinateType.LUNATIC);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol, cellType.LUNATIC);
									removeLunatics();
									Main.GAME_INSTANCE.gameLost(); /// Game over
									break mainLoop;
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
									
								if (getCellType(nR, nC, cellType.PLAYER)) { // Catches the player
									
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, nR, nC, coordinateType.LUNATIC_CHASING);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, nR, nC, cellType.LUNATIC_CHASING);
									removeLunatics();
									Main.GAME_INSTANCE.gameLost(); // Game over
									break mainLoop;
									
								} else {
								
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, nR, nC, coordinateType.LUNATIC_CHASING);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, nR, nC, cellType.LUNATIC_CHASING);
									lunatic.updateLunaticState(currentPlayerLocation);
								}
							}
							if(lunatic.getLunaticState() == lunaticState.investigating) { // INVESTIGATING LOGIC
								
								currentRow = lunatic.getCurrentLocation().getRow();
								currentCol = lunatic.getCurrentLocation().getColumn();
								Coordinate nextMoveToPlayer = lunatic.getNextMoveToPlayer();
								int nR = nextMoveToPlayer.getRow();
								int nC = nextMoveToPlayer.getColumn();
								lunatic.setCurrentLocation(nR, nC);
								
								if (getCellType(nR, nC, cellType.PLAYER)) { // Catches the player
									
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, nR, nC, coordinateType.LUNATIC_CHASING);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, nR, nC, cellType.LUNATIC_CHASING);
									removeLunatics();
									Main.GAME_INSTANCE.gameLost(); // Game over
									break mainLoop;
										
								} else {
									
									GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, nR, nC, coordinateType.LUNATIC_CHASING);
									GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, nR, nC,cellType.LUNATIC_CHASING);
									lunatic.updateLunaticState(currentPlayerLocation);
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
