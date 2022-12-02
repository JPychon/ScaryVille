package controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import scaryville.Main;

public class MouseController implements EventHandler<MouseEvent>  {

	@Override
	public void handle(MouseEvent event) {
		if(MouseButton.PRIMARY.equals(event.getButton())) { // Left click
			
			GameController.GUI_CONTROLLER.resetRootNode(); // Reset the gridpane when the button is clicked.
			Main.GAME_INSTANCE.startNewGame(); // Starts a new game
		}
	}
}
