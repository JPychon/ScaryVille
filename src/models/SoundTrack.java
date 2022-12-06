package models;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundTrack {
	
	private Media game_loop_track;
	private Media game_win_track;
	private Media game_lost_track;
	
	private MediaPlayer game_loop_player;
	private MediaPlayer game_win_player;
	private MediaPlayer game_lost_player;
	
	public MediaPlayer getGameLoopPlayer() { return game_loop_player; } // Soundtrack during standard gameplay
	public MediaPlayer getGameWinPlayer() { return game_win_player; } // Soundtrack when the player wins the game
	public MediaPlayer getGameLossPlayer() { return game_lost_player; } // Soundtrack when the player wins the game
	
	public SoundTrack() 
	{
			game_loop_track = new Media(getClass().getResource("/gameLoop.mp3").toExternalForm());
			game_win_track = new Media(getClass().getResource("/gameWin.mp3").toExternalForm());
			game_lost_track = new Media(getClass().getResource("/gameLost.mp3").toExternalForm());
			game_loop_player = new MediaPlayer(game_loop_track);
			game_win_player = new MediaPlayer(game_win_track);
			game_lost_player = new MediaPlayer(game_lost_track);
	}

}
