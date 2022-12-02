package scaryville;
	
import controllers.GameController;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application { 
	
	public static GameController GAME_INSTANCE = new GameController(); // Static instance of the game controller 
	
	public int rowNum = 30; // Total number of rows to be used.
	public int colNum = 50; // Total number of columns to be used.
	public double wallCoeff = 0.18; // wall percentage coefficient
	
	@Override
	public void start(Stage mainStage) throws Exception {
		
		GAME_INSTANCE.Initalize(mainStage, rowNum, colNum, wallCoeff); // Initalize the game
	}

	public static void main(String[] args) {
		launch(args);
	}
}
