package controllers;


import models.Coordinate;
import models.Lunatic;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import models.Cell.cellType;
import models.Lunatic.lunaticNextMove;
import models.Lunatic.lunaticState;
import models.Tulpe4;
import models.Coordinate.coordinateType;



public class LunaticController {

	private final int MAX_LUNATICS = 5;
	
	private List<Lunatic> lunatics = new ArrayList<Lunatic>(); // List of lunatics chasing the player

	Timeline roamTimer; // Timeline for the lunatics roam
	
	public LunaticController() { }
	
	public List<Lunatic> getLunatics() { return lunatics; }
	
	public void createLunatics() { // Creates the lunatics in random-valid locations on the map

		int spawned_lunatics = MAX_LUNATICS;
		boolean lunaticSpawned = false;

		Coordinate[][] boardState = GameController.BOARD_CONTROLLER.getBoard().getMapGrid();

		// This is ugly code, any tips on how to implement it better?
		// It's meant to loop through the 2D array, pick a value, check the coordinate
		// type & spawn the lunatic if it's valid.
		// To avoid cluster, I tried iterating by +10
		// the inner loops iteration per successful new instance

		for (int row = 5; row < boardState.length - 1; row += (lunaticSpawned ? 10 : 1)) { 
			for (int col = 15; col < boardState[row].length - 1; col += (lunaticSpawned ? 10 : 1)) { 

				if (spawned_lunatics == 0)
					break;
				if (boardState[row][col].getCoordinateType() == coordinateType.BLANK) {

					Lunatic lunaticInstance = new Lunatic();
					lunaticInstance.setLunaticState(lunaticState.idle_roam);
					lunaticInstance.setSpawnLocation(row, col);
					lunatics.add(lunaticInstance);

					GameController.BOARD_CONTROLLER.updateBoardState(row, col, row, col, coordinateType.LUNATIC);
					GameController.GUI_CONTROLLER.updateGUIState(row, col, row, col, coordinateType.LUNATIC);

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
			for (int col = 15; col < boardState[row].length - 1; col += (lunaticSpawned ? 10 : 1)) { 

				if (spawned_lunatics == 0)
					break;
				if (boardState[row][col].getCoordinateType() == coordinateType.BLANK) {

					lunatics.get(spawned_lunatics).setSpawnLocation(row, col);

					GameController.BOARD_CONTROLLER.updateBoardState(row, col, row, col, coordinateType.LUNATIC);
					GameController.GUI_CONTROLLER.updateGUIState(row, col, row, col, coordinateType.LUNATIC);

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
			GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, 0, 0, coordinateType.BLANK); // Resets the next-cell to blank/path	
			
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

	public void roamLunatics() { // Moves the lunatics in random-directions based on a timeline (1.5 seconds)
		
			roamTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
				int currentRow, currentCol, newRow, newCol;

				for (int lunaticsCount = 0; lunaticsCount < lunatics.size(); lunaticsCount++) {

					if(lunatics.get(lunaticsCount).getLunaticState() == lunaticState.idle_roam) {
						
						currentRow = lunatics.get(lunaticsCount).getCurrentLocation().getRow();
						currentCol = lunatics.get(lunaticsCount).getCurrentLocation().getColumn();

						lunaticNextMove move = lunatics.get(lunaticsCount).roam();
						switch (move) {

						case UP:

							newRow = currentRow - 1;
							newCol = currentCol;

							if (GameController.GUI_CONTROLLER.getGUI().getGridCellType(newRow - 1,
									newCol) == cellType.PATH) {

								lunatics.get(lunaticsCount).setCurrentLocation(newRow, newCol);
								GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol,
										coordinateType.LUNATIC);
								GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol,
										coordinateType.LUNATIC);
								break;
							}

							break;

						case DOWN:

							newRow = currentRow + 1;
							newCol = currentCol;

							if (GameController.GUI_CONTROLLER.getGUI().getGridCellType(newRow - 1,
									newCol) == cellType.PATH) {

								lunatics.get(lunaticsCount).setCurrentLocation(newRow, newCol);
								GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol,
										coordinateType.LUNATIC);
								GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol,
										coordinateType.LUNATIC);
								break;
							}

							break;

						case LEFT:

							newRow = currentRow;
							newCol = currentCol - 1;

							if (GameController.GUI_CONTROLLER.getGUI().getGridCellType(newRow - 1,
									newCol) == cellType.PATH) {

								lunatics.get(lunaticsCount).setCurrentLocation(newRow, newCol);
								GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol,
										coordinateType.LUNATIC);
								GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol,
										coordinateType.LUNATIC);
								break;
							}

							break;

						case RIGHT:

							newRow = currentRow;
							newCol = currentCol - 1;

							if (GameController.GUI_CONTROLLER.getGUI().getGridCellType(newRow - 1,
									newCol) == cellType.PATH) {

								lunatics.get(lunaticsCount).setCurrentLocation(newRow, newCol);
								GameController.BOARD_CONTROLLER.updateBoardState(currentRow, currentCol, newRow, newCol,
										coordinateType.LUNATIC);
								GameController.GUI_CONTROLLER.updateGUIState(currentRow, currentCol, newRow, newCol,
										coordinateType.LUNATIC);
								break;
							}

							break;
						default:
							break;
						}
						
					} else if(lunatics.get(lunaticsCount).getLunaticState() == lunaticState.chasing) { // CHASING LOGIC
						
					
						
						
						
					}

				} 
			}
		));
			
		updateLunaticsLOS();
		lunaticStateCheck();	
		roamTimer.setCycleCount(Animation.INDEFINITE);
		roamTimer.playFromStart();
	}
			
	public void lunaticStateCheck() {
		
		Coordinate currentPlayerLocation = GameController.PLAYER_CONTROLLER.getPlayerLocation();
		
		for(Lunatic l : lunatics) {
			
			if(l.getVerticalLineOfSight_down().contains(currentPlayerLocation) || l.getVerticalLineOfSight_down().contains(currentPlayerLocation)) {
				l.setLunaticState(lunaticState.chasing);
			}
			
			if(l.getHorizontalLineOfSight_left().contains(currentPlayerLocation) || l.getHorizontalLineOfSight_right().contains(currentPlayerLocation)) {
				l.setLunaticState(lunaticState.chasing);
				
			} else {
				l.setLunaticState(lunaticState.idle_roam);
			}
		}
	}
		
	

	public void updateLunaticsLOS() {
		
			int currentRow, currentCol;
			for(int lunatic = 0; lunatic < lunatics.size(); lunatic++) {
				
					List<Coordinate> up_LOS = new ArrayList<Coordinate>();
					List<Coordinate> down_LOS = new ArrayList<Coordinate>();
					List<Coordinate> left_LOS = new ArrayList<Coordinate>();
					List<Coordinate> right_LOS = new ArrayList<Coordinate>();
					
					
					Tulpe4<List<Coordinate>, List<Coordinate>, List<Coordinate>, List<Coordinate>> lines_of_sight = new Tulpe4
					<List<Coordinate>, List<Coordinate>, List<Coordinate>, List<Coordinate>> (up_LOS, down_LOS, left_LOS, right_LOS);
				
					currentRow = lunatics.get(lunatic).getCurrentLocation().getRow();
					currentCol = lunatics.get(lunatic).getCurrentLocation().getColumn();
					lines_of_sight = GameController.GUI_CONTROLLER.getClearCellsFromCoordinate(currentRow, currentCol);
					
					lunatics.get(lunatic).setVerticalLineOfSight_up(lines_of_sight.getFirst());
					lunatics.get(lunatic).setVerticalLineOfSight_down(lines_of_sight.getSecond());
					lunatics.get(lunatic).setHorizontalLineOfSight_left(lines_of_sight.getThird());
					lunatics.get(lunatic).setHorizontalLineOfSight_right(lines_of_sight.getFourth());
			}
		}
}
