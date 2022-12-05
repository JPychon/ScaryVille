package controllers;

import javafx.animation.Timeline;
import javafx.stage.Stage;

public class GameController {
	
	public static GUIController GUI_CONTROLLER; // Static instance of the GUI Controller
	public static BoardController BOARD_CONTROLLER; // Static instance of the Board Controller
	public static PlayerController PLAYER_CONTROLLER; // Static instance of the Player Controller
	public static LunaticController LUNATIC_CONTROLLER; // Static instance of the Lunatic Controller
	
	public static boolean isPaused = false; // Game pause status
	public static boolean isLost = false; // Game lost status
	public static boolean isGridHidden = false; // Grid blacked out for pauses/new-game/lost
	
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
		
		PLAYER_CONTROLLER.createPlayer(); // Create new player instance
		LUNATIC_CONTROLLER.removeLunatics(); // Delete all old lunatics
		LUNATIC_CONTROLLER.createLunatics(); // Create new lunatics
		LUNATIC_CONTROLLER.roamLunatics(); // Start their movement & pathfinding logic
		lunaticRoam.playFromStart(); // Start their animation
		isPaused = false;
		isLost = false;
		isGridHidden = false;
	}
	
	public void pauseGame() {
		
		Timeline lunaticRoam = LUNATIC_CONTROLLER.getRoamTimerAnimation(); // Returns the instance of the lunatic movement-timer/animation
		if(isPaused) {
			GameController.GUI_CONTROLLER.unhideGrid();
			isGridHidden = false;
			isPaused = false;
			lunaticRoam.playFromStart();
			
		} else {
			
			isPaused = true;
			GameController.GUI_CONTROLLER.hideGrid();
			isGridHidden = true;
			lunaticRoam.stop();
		}
	}
	
	public void gameLost() { // Stops the game when the player loses to the lunatics.
		
		pauseGame();
		isLost = true;
		//startNewGame();
	}
}
