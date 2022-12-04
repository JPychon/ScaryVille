package controllers;

import controllers.PlayerController.playerMovement;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import scaryville.Main;

public class InputController implements EventHandler<KeyEvent> {

	@Override
	public void handle(final KeyEvent eventKey) {
	
		if(GameController.PLAYER_CONTROLLER.getPlayerControlStatus()) { // Check if player control is enabled
			
			switch(eventKey.getCode()) {
			
			case UP: GameController.PLAYER_CONTROLLER.updatePlayerLocation(playerMovement.UP); break;
			case DOWN: GameController.PLAYER_CONTROLLER.updatePlayerLocation(playerMovement.DOWN); break;
			case LEFT: GameController.PLAYER_CONTROLLER.updatePlayerLocation(playerMovement.LEFT); break;
			case RIGHT: GameController.PLAYER_CONTROLLER.updatePlayerLocation(playerMovement.RIGHT); break;
			
			case F10: GameController.GUI_CONTROLLER.resetRootNode(); Main.GAME_INSTANCE.startNewGame(); break;
			case HOME: Main.GAME_INSTANCE.pauseGame(); break; // Pauses the game
					
			default: break;
			
			}
		}
	}
}
