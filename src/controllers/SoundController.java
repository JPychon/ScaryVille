package controllers;

import javafx.scene.media.MediaPlayer;
import models.SoundTrack;

public class SoundController {

	private MediaPlayer gameTrack;
	private MediaPlayer winTrack;
	private MediaPlayer lostTrack;
	
	public SoundController() 
	{
		SoundTrack SoundTrackData = new SoundTrack();
		gameTrack = SoundTrackData.getGameLoopPlayer();
		winTrack = SoundTrackData.getGameWinPlayer();
		lostTrack = SoundTrackData.getGameLossPlayer();
	}
	
	public void playGameTrack() { gameTrack.play(); gameTrack.setVolume(0.5); }
	public void pauseGameTrack() { gameTrack.pause(); }
	public void stopGameTrack() { gameTrack.stop(); }
	public void playWinTrack() { winTrack.play(); gameTrack.setVolume(0.5); }
	public void stopWinTrack() { winTrack.stop(); }
	public void playLostTrack() { lostTrack.play(); lostTrack.setVolume(0.5); }
	public void stopLostTrack() { lostTrack.stop(); }
}
