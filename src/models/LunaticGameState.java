package models;

import java.util.List;

import controllers.GameController;
import controllers.LunaticController;

public class LunaticGameState {

	
	
	public LunaticGameState() {
		
	}
	
	public void GameLoop() {
		List<Lunatic> lunatics = GameController.LUNATIC_CONTROLLER.getLunatics();
		while(true) { // TODO: Game state running
			
			GameController.LUNATIC_CONTROLLER.updateLunaticsLOS();
			
		}
	}
}
