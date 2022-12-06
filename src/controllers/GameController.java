package controllers;

import javafx.stage.Stage;

public class GameController {
	
	public static GUIController GUI_CONTROLLER; // Static instance of the GUI Controller
	public static BoardController BOARD_CONTROLLER; // Static instance of the Board Controller
	public static PlayerController PLAYER_CONTROLLER; // Static instance of the Player Controller
	public static LunaticController LUNATIC_CONTROLLER; // Static instance of the Lunatic Controller
	public static SoundController SOUND_CONTROLLER; // Static instance of the Sound Controller
	
	public static boolean isPaused = false; // Game pause status
	public static boolean isLost = false; // Game lost status
	public static boolean isWon = false; // Game won status
	public static boolean isGridHidden = false; // Grid blacked out for pauses/new-game/lost
	
	public GameController() {  }
	public boolean isGamePaused() { return isPaused; }
	
	public  void Initalize(Stage GUI_stage, int rows, int columns, double wallCoeff) {
		
		BOARD_CONTROLLER = new BoardController(rows, columns, wallCoeff); // Initalizes the Board controller
		GUI_CONTROLLER = new GUIController(GUI_stage, rows, columns); // Initalizes the GUI controller
		LUNATIC_CONTROLLER = new LunaticController(); // Initalizesd the Lunatic controller
		PLAYER_CONTROLLER = new PlayerController(); // Initalizes the Player controller
		SOUND_CONTROLLER = new SoundController(); // Initalizes the Sound Controller
		
		BOARD_CONTROLLER.generateBoard(); // Generates the board logic (AsylumMap)
		GUI_CONTROLLER.populateGrid(true); // Generates the GUI (MapPane)
		PLAYER_CONTROLLER.createPlayer(); // Creates the Player (Player)
		LUNATIC_CONTROLLER.createLunatics(); // Creates the Lunatics (lunatic)
		
		GUI_CONTROLLER.initalizeMainStage();
		
		LUNATIC_CONTROLLER.roamLunatics();
		GameController.SOUND_CONTROLLER.playGameTrack();
	}
	
	public void startNewGame() { // Starts a new game with a new layout
		
		if(isLost == false) { GameController.LUNATIC_CONTROLLER.disableLunatics(); }
		if(isLost == true) { GameController.SOUND_CONTROLLER.stopLostTrack(); }
		if(isWon == true) { GameController.SOUND_CONTROLLER.stopWinTrack(); }
		
		GameController.GUI_CONTROLLER.hideGrid_instant(); // Hide the grid
		
		PLAYER_CONTROLLER.createPlayer(); // Create new player instance
		LUNATIC_CONTROLLER.createLunatics(); // Create new lunatics
		LUNATIC_CONTROLLER.roamLunatics(); // Start their movement & pathfinding logic
		
		GameController.GUI_CONTROLLER.unhideGrid(); // Show the grid
		GameController.SOUND_CONTROLLER.playGameTrack();  // Play the ga,e loop track
		
		// Reset all flags
		isPaused = false; 
		isLost = false;
		isWon = false;
		isGridHidden = false;
	}
	
	public void pauseGame(boolean isPause, boolean isLost, boolean isWin)  // Pauses the game
	{
		if(isPaused) 
		{
			GameController.GUI_CONTROLLER.unhideGrid();
			GameController.LUNATIC_CONTROLLER.enableLunatics();
			isGridHidden = false;
			isPaused = false;
			GameController.SOUND_CONTROLLER.playGameTrack();
			
		} else {
			
			isPaused = true;
			GameController.GUI_CONTROLLER.hideGrid(isPause, isLost, isWin); 
			GameController.LUNATIC_CONTROLLER.disableLunatics();
			isGridHidden = true;
			GameController.SOUND_CONTROLLER.pauseGameTrack();
		}
	}
	
	public void gameLost() // Stops the game when the player loses to the lunatics.
	{ 
		isLost = true;
		GameController.GUI_CONTROLLER.hideGrid(false, true, false);
		isGridHidden = true;
		GameController.SOUND_CONTROLLER.stopGameTrack();
		GameController.SOUND_CONTROLLER.playLostTrack();
	}
	
	public void gameWon() { isWon = true; GameController.SOUND_CONTROLLER.playWinTrack();}
}
