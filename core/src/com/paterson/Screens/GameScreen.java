package com.paterson.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.input.GestureDetector;
import com.paterson.CowPat.CowPat;
import com.paterson.GameWorld.GameRenderer;
import com.paterson.GameWorld.GameWorld;
import com.paterson.Helpers.AssetLoader;
import com.paterson.Helpers.InputHandler;

public class GameScreen implements Screen {

	private GameWorld world;
	private GameRenderer renderer;
	private float runTime;

	public static final int GAME_WIDTH = 136;

	/**
	 * GameScreen constructor
	 */
	public GameScreen(CowPat game) {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float gameWidth = GameScreen.GAME_WIDTH;
		float gameHeight = screenHeight / (screenWidth / gameWidth);
		int midPointY = (int) (gameHeight / 2);
		//System.out.println("GameHeight: " + gameHeight + "   GameWidth: " + gameWidth + "   ScreenWidth: " + screenWidth + "   ScreenHeight: " + screenHeight);

		world = new GameWorld(game,(int) gameHeight);
		InputHandler iHandler = new InputHandler(world, screenWidth / gameWidth, screenHeight / gameHeight);
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(iHandler);
		im.addProcessor(gd);
		im.addProcessor(iHandler);

		Gdx.input.setInputProcessor(im);
		world.setInputHandler(iHandler);
		renderer = new GameRenderer(world, (int) gameHeight, midPointY);
	}

	@Override
	public void render(float delta) {
		runTime += delta;
		world.update(delta);
		renderer.render(delta, runTime);
	}

	@Override
	public void resize(int width, int height) {
		if (world.isRunning() || world.isThrowingRunning())
		{
			world.pauseGame();
		}
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		if (world.isRunning() || world.isThrowingRunning())
		{
			world.pauseGame();
		}
	}

	@Override
	public void resume() {
		//if (world.isGamePaused())
		//{
		//	world.unPause();
		//}
	}

	@Override
	public void dispose() {
		AssetLoader.dispose();
	}
	
}
