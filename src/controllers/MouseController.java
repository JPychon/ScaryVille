package controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import scaryville.Main;

public class MouseController implements EventHandler<Event>  {

	@Override
	public void handle(Event event) 
	{
		String ID = ((Control)event.getSource()).getId(); // Returns the ID of the button pressed
		switch(ID) 
		{
			case "hbox-new-game-button": GameController.GUI_CONTROLLER.resetRootNode(); Main.GAME_INSTANCE.startNewGame(); break;
			case "hbox-pause-game-button": if(!GameController.isLost && !GameController.isWon) { Main.GAME_INSTANCE.pauseGame(true, false, false); } break;
		}
	}
}
