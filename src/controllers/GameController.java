package controllers;

import javafx.animation.Timeline;
import javafx.stage.Stage;

public class GameController {
	
	public static GUIController GUI_CONTROLLER; // Static instance of the GUI Controller
	public static BoardController BOARD_CONTROLLER; // Static instance of the Board Controller
	public static PlayerController PLAYER_CONTROLLER; // Static instance of the Player Controller
	public static LunaticController LUNATIC_CONTROLLER; // Static instance of the Lunatic Controller
	
	private static boolean isPaused = false; // Game pause status
	
	public GameController() {  }
	public boolean isGamePaused() { return isPaused; }
	
	public  void Initalize(Stage GUI_stage, int rows, int columns, double wallCoeff) {
		
		BOARD_CONTROLLER = new BoardController(rows, columns, wallCoeff); // Initalizes the Board controller
		GUI_CONTROLLER = new GUIController(GUI_stage, rows, columns); // Initalizes the GUI controller
		LUNATIC_CONTROLLER = new LunaticController(); // Initalizesd the Lunatic controller
		PLAYER_CONTROLLER = new PlayerController(); // Initalizes the Player controller
		
		BOARD_CONTROLLER.generateBoard(); // Generates the board logic (AsylumMap)
		GUI_CONTROLLER.populateGrid(true); // Generates the GUI (MapPane)
		PLAYER_CONTROLLER.createPlayer(); // Creates the Player (Player)
		LUNATIC_CONTROLLER.createLunatics(); // Creates the Lunatics (lunatic)
		
		GUI_CONTROLLER.initalizeMainStage();
		
		LUNATIC_CONTROLLER.roamLunatics();
	}
	
	public void startNewGame() {

		Timeline lunaticRoam = LUNATIC_CONTROLLER.getRoamTimerAnimation(); // Returns the instance of the lunatic movement-timer/animation
		
		PLAYER_CONTROLLER.createPlayer();
		LUNATIC_CONTROLLER.respawnLunatics();
		LUNATIC_CONTROLLER.roamLunatics();
		lunaticRoam.playFromStart();
	}
	
	public void pauseGame() {
		
		Timeline lunaticRoam = LUNATIC_CONTROLLER.getRoamTimerAnimation(); // Returns the instance of the lunatic movement-timer/animation
		
		if(isPaused) {
			
			isPaused = false;
			lunaticRoam.playFromStart();
			//System.out.println("[DEBUG::GAME_CONTROLLER] Game [UNPAUSED]");
			
		} else {
			
			isPaused = true;
			lunaticRoam.stop();
			//System.out.println("[DEBUG::GAME_CONTROLLER] Game [PAUSED]");
		}
	}
	
	public void gameLost() { // Stops the game when the player loses to the lunatics.
		
		pauseGame(); // Pauses the game [disables lunatics movement too]
		startNewGame();
	}
}
