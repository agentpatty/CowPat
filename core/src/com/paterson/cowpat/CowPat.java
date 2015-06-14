package com.paterson.cowpat;


import com.badlogic.gdx.Game;
import com.paterson.Helpers.AssetLoader;
import com.paterson.Helpers.IPictureRetriever;
import com.paterson.Screens.SplashScreen;



public class CowPat extends Game {

	IPictureRetriever pictureChooser;
	
	public CowPat(IPictureRetriever pictureChooser)
	{
		super();
		this.pictureChooser = pictureChooser;
	}
	
	@Override
	public void create() {
		AssetLoader.load();
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
		System.out.println("Dispose on Game called.");
	}
	
	public IPictureRetriever getPictureChooser()
	{
		return this.pictureChooser;
	}

	@Override
	public void pause() {
		super.pause();
		System.out.println("Game Paused");
	}

	@Override
	public void resume() {
		super.resume();
		System.out.println("Game Resumed");
	}
	
	
}
