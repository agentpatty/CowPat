package com.paterson.CowPat;


import com.badlogic.gdx.Game;
import com.paterson.Helpers.AssetLoader;
import com.paterson.Screens.SplashScreen;


public class CowPat extends Game {

	public CowPat()
	{
		super();
	}

	@Override
	public void create () {
		AssetLoader.load();
		setScreen(new SplashScreen(this));
	}


	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}
}
