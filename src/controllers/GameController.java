package controllers;

import javafx.stage.Stage;

public class GameController {
	
	public static GUIController GUI_CONTROLLER; // Static instance of the GUI Controller
	public static BoardController BOARD_CONTROLLER; // Static instance of the Board Controller
	public static PlayerController PLAYER_CONTROLLER; // Static instance of the Player Controller
	public static LunaticController LUNATIC_CONTROLLER; // Static instance of the Lunatic Controller
	
	public GameController() {  }
	
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
		
		PLAYER_CONTROLLER.createPlayer();
		LUNATIC_CONTROLLER.respawnLunatics();
		LUNATIC_CONTROLLER.roamLunatics();
	}
}
