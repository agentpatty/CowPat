package com.paterson.Helpers;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.paterson.GameWorld.GameRenderer;
import com.paterson.GameWorld.GameWorld;
import com.paterson.UI.ClickableObject;
import com.paterson.UI.SimpleCheckBox;
import com.paterson.UI.SimpleImageButton;
import com.paterson.UI.SimpleTextButton;

public class InputHandler implements InputProcessor, GestureDetector.GestureListener {

	public static final float MIN_FLING_SPEED_REQUIRED = -300;

	private GameWorld myWorld;

	private List<ClickableObject> menuButtons, aboutButtons, settingsOptions, pausedButtons, levelCompleteButtons;

	private ClickableObject newGameButton, settingsButton, aboutButton, soundCheckbox, musicCheckbox, backButton, /**changeTargetButton,**/ clearHighScoreButton,
			tryAgainButton, returnToMenuButton, /**exitButton,**/
			pausedResumeButton, pausedExitButton, pausedSettingsButton, pauseButton, rightTargetButton, leftTargetButton;

	private float scaleFactorX;
	private float scaleFactorY;

	public InputHandler(GameWorld myWorld, float scaleFactorX,
						float scaleFactorY) {
		this.myWorld = myWorld;

		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;

		// Pause button used to pause the game while it is running
		pauseButton = new SimpleImageButton(7, 7, AssetLoader.pauseButton, AssetLoader.pauseButton);

		// Create the buttons for the main menu
		newGameButton = new SimpleTextButton(80, 8, AssetLoader.translations.get(GameRenderer.KEY_NEW_GAME));
		aboutButton = new SimpleTextButton(100, 8, AssetLoader.translations.get(GameRenderer.KEY_HOW_TO_PLAY));
		settingsButton = new SimpleTextButton(75, 8, AssetLoader.translations.get(GameRenderer.KEY_SETTINGS));
		//exitButton = new SimpleTextButton(35, 8, "Exit");
		menuButtons = new ArrayList<ClickableObject>();
		menuButtons.add(newGameButton);
		menuButtons.add(aboutButton);
		menuButtons.add(settingsButton);
//		menuButtons.add(exitButton);

		// Create the options to be selected in the Settings menu
		soundCheckbox = new SimpleCheckBox(10, 10, AssetLoader.getSoundsSetting());
		musicCheckbox = new SimpleCheckBox(10, 10, AssetLoader.getMusicSetting());
		backButton = new SimpleTextButton(35, 8, AssetLoader.translations.get(GameRenderer.KEY_BACK));
		//changeTargetButton = new SimpleTextButton(10, 8, "Select new Target");
		rightTargetButton = new SimpleImageButton(10, 15, AssetLoader.rightArrow, AssetLoader.rightArrow);
		leftTargetButton = new SimpleImageButton(10, 15, AssetLoader.leftArrow, AssetLoader.leftArrow);
		clearHighScoreButton = new SimpleTextButton(100, 8, AssetLoader.translations.get(GameRenderer.KEY_CLEAR_HIGH_SCORE));
		settingsOptions = new ArrayList<ClickableObject>();
		settingsOptions.add(soundCheckbox);
		settingsOptions.add(musicCheckbox);
		settingsOptions.add(backButton);
		//settingsOptions.add(changeTargetButton);
		settingsOptions.add(rightTargetButton);
		settingsOptions.add(leftTargetButton);
		settingsOptions.add(clearHighScoreButton);

		// Buttons for the about screen - only has a back button
		aboutButtons = new ArrayList<ClickableObject>();
		aboutButtons.add(backButton); // This was created above

		// Buttons displayed when level complete
		tryAgainButton = new SimpleTextButton(65, 8, AssetLoader.translations.get(GameRenderer.KEY_TRY_AGAIN));
		returnToMenuButton = new SimpleTextButton(80, 8, AssetLoader.translations.get(GameRenderer.KEY_BACK_TO_MENU));
		levelCompleteButtons = new ArrayList<ClickableObject>();
		levelCompleteButtons.add(tryAgainButton);
		levelCompleteButtons.add(returnToMenuButton);

		// All the buttons for the paused menu
		pausedResumeButton = new SimpleTextButton(40, 8, AssetLoader.translations.get(GameRenderer.KEY_RESUME));
		pausedSettingsButton = new SimpleTextButton(45, 8, AssetLoader.translations.get(GameRenderer.KEY_SETTINGS));
		pausedExitButton = new SimpleTextButton(24,8,AssetLoader.translations.get(GameRenderer.KEY_QUIT));
		pausedButtons = new ArrayList<ClickableObject>();
		pausedButtons.add(pausedExitButton);
		pausedButtons.add(pausedResumeButton);
		pausedButtons.add(pausedSettingsButton);
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		if (myWorld.isMainMenu())
		{
			for (ClickableObject obj : menuButtons)
			{
				obj.isTouchDown(screenX, screenY);
			}
		}
		else if (myWorld.isSettingsMenu())
		{
			for (ClickableObject obj : settingsOptions)
			{
				obj.isTouchDown(screenX, screenY);
			}
		}
		else if (myWorld.isAboutMenu())
		{
			for (ClickableObject obj : aboutButtons)
			{
				obj.isTouchDown(screenX, screenY);
			}
		}
		else if (myWorld.isGamePaused())
		{
			for (ClickableObject obj : pausedButtons)
			{
				obj.isTouchDown(screenX, screenY);
			}
		}
		else if (myWorld.isReady()) {
			myWorld.start();
		}
		else if (myWorld.isThrowingReady())
		{
			myWorld.startThrowing();
		}
		else if (myWorld.isLevelComplete())
		{
			myWorld.prepareThrowing();
		}
		else if (myWorld.isThrowingComplete())
		{
			myWorld.ready();
		}
		else if (myWorld.isRunning()) {
			if (screenX < (myWorld.getCatcher().getMidPoint()))
			{
				myWorld.moveLeft();
			}
			else
			{
				myWorld.moveRight();
			}
			pauseButton.isTouchDown(screenX, screenY);
		}
		else if (myWorld.isThrowingRunning())
		{
			pauseButton.isTouchDown(screenX, screenY);
		}

		if (myWorld.isGameOver() || myWorld.isHighScore()) {
			for (ClickableObject obj : levelCompleteButtons)
			{
				obj.isTouchDown(screenX, screenY);
			}
		}
		return true;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// Gesture listener method
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
		//System.out.println("Current X: " + screenX);
		//System.out.println("Current Y: " + screenY);
		if (myWorld.isMainMenu())
		{
			if (newGameButton.isTouchUp(screenX, screenY))
			{
				myWorld.ready();
				return true;
			}
			else if (settingsButton.isTouchUp(screenX, screenY))
			{
				myWorld.goToSettings();
				return true;
			}
			else if (aboutButton.isTouchUp(screenX, screenY))
			{
				myWorld.goToAbout();
				return true;
			}
//			else if (exitButton.isTouchUp(screenX, screenY))
//			{
//				myWorld.exitGame();
//				return true;
//			}
		}
		else if (myWorld.isSettingsMenu())
		{
			if (soundCheckbox.isTouchUp(screenX, screenY))
			{
				// Change the setting in the preferences
				AssetLoader.setSoundSetting(((SimpleCheckBox)soundCheckbox).isChecked());
				return true;
			}
			if (musicCheckbox.isTouchUp(screenX, screenY))
			{
				// Change the setting in the preferences
				AssetLoader.setMusicSetting(((SimpleCheckBox)musicCheckbox).isChecked());
				return true;
			}
//			if (changeTargetButton.isTouchUp(screenX, screenY))
//			{
//				// Popup to select a photo from your gallery
//				myWorld.selectNewTarget();
//				return true;
//			}
			if (leftTargetButton.isTouchUp(screenX, screenY))
			{
				myWorld.setPreviousTarget();
				return true;
			}
			if (rightTargetButton.isTouchUp(screenX, screenY))
			{
				myWorld.setNextTarget();
				return true;
			}
			if (clearHighScoreButton.isTouchUp(screenX, screenY))
			{
				myWorld.setHighScore(0);
				return true;
			}
			if (backButton.isTouchUp(screenX, screenY))
			{
				myWorld.goToBackFromSettingsMenu();
				return true;
			}
		}
		else if (myWorld.isAboutMenu())
		{
			if (backButton.isTouchUp(screenX, screenY))
			{
				myWorld.goToMainMenu();
				return true;
			}
		}
		else if (myWorld.isGamePaused())
		{
			if (pausedResumeButton.isTouchUp(screenX, screenY))
			{
				myWorld.unPause();
				return true;
			}
			else if (pausedSettingsButton.isTouchUp(screenX, screenY))
			{
				// TODO Need to make some changes here - perhaps take in a boolean to keep it paused
				myWorld.goToSettings();
				return true;
			}
			else if (pausedExitButton.isTouchUp(screenX, screenY))
			{
				myWorld.resetGame();
				myWorld.goToMainMenu();
				return true;
			}
		}
		else if (myWorld.isRunning()) {
			if (pauseButton.isTouchUp(screenX, screenY))
			{
				myWorld.pauseGame();
				return true;
			}
		}
		else if (myWorld.isThrowingRunning())
		{
			if (pauseButton.isTouchUp(screenX, screenY))
			{
				myWorld.pauseGame();
				return true;
			}
		}
		else if (myWorld.isGameOver() || myWorld.isHighScore())
		{
			if (tryAgainButton.isTouchUp(screenX, screenY))
			{
				myWorld.restartGame();
				return true;
			}
			else if (returnToMenuButton.isTouchUp(screenX, screenY))
			{
				myWorld.resetGame();
				myWorld.goToMainMenu();
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
			case Keys.LEFT:
				if (myWorld.isRunning())
				{
					myWorld.moveLeft();
				}
				break;
			case Keys.RIGHT:
				if (myWorld.isRunning())
				{
					myWorld.moveRight();
				}
				break;
			case Keys.SPACE:
				if (myWorld.isReady()) {
					myWorld.start();
				}

				if (myWorld.isGameOver()) {
					myWorld.restartGame();
				}
				break;
			case Keys.P:
				if (myWorld.isRunning() || myWorld.isThrowingRunning())
				{
					myWorld.pauseGame();
				}
			case Keys.N:
				if (myWorld.isRunning())
				{
					myWorld.levelSkip();
				}
			default:
				break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//System.out.println("Current X: " + screenX);
		if (myWorld.isThrowingRunning())
		{
			myWorld.setThrowingContainerX(scaleX(screenX));
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	private int scaleX(int screenX) {
		return (int) (screenX / scaleFactorX);
	}

	private int scaleY(int screenY) {
		return (int) (screenY / scaleFactorY);
	}


	public ClickableObject getNewGameButton() {
		return newGameButton;
	}


	public ClickableObject getSettingsButton() {
		return settingsButton;
	}


	public ClickableObject getAboutButton() {
		return aboutButton;
	}


	public ClickableObject getSoundCheckbox() {
		return soundCheckbox;
	}


	public ClickableObject getMusicCheckbox() {
		return musicCheckbox;
	}


	public ClickableObject getBackButton() {
		return backButton;
	}


//	public ClickableObject getTargetButton() {
//		return changeTargetButton;
//	}


	public ClickableObject getPausedResumeButton() {
		return pausedResumeButton;
	}


	public ClickableObject getPausedExitButton() {
		return pausedExitButton;
	}

	public ClickableObject getpausedSettingsButton() {
		return pausedSettingsButton;
	}

	public ClickableObject getPauseButton()
	{
		return pauseButton;
	}

	public ClickableObject getClearHighScoreButton() {
		return clearHighScoreButton;
	}


	public ClickableObject getTryAgainButton() {
		return tryAgainButton;
	}


	public ClickableObject getReturnToMenuButton() {
		return returnToMenuButton;
	}


//	public ClickableObject getExitButton() {
//		return exitButton;
//	}

	public ClickableObject getRightArrowButton()
	{
		return rightTargetButton;
	}

	public ClickableObject getLeftArrowButton()
	{
		return leftTargetButton;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// Do nothing
		return false;
	}


	@Override
	public boolean longPress(float x, float y) {
		// Do nothing
		return false;
	}


	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// Flung!!
		if (myWorld.isThrowingRunning() && velocityY < MIN_FLING_SPEED_REQUIRED)
		{
			//System.out.println("Flung poo!!! VelocityX = " + velocityX  + "  VelocityY = " + velocityY);
			myWorld.throwPooAtTarget(velocityY, velocityY);
			return true;
		}
		return false;
	}


	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// Do nothing
		return false;
	}


	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// Do nothing
		return false;
	}


	@Override
	public boolean zoom(float initialDistance, float distance) {
		// Do nothing
		return false;
	}


	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
						 Vector2 pointer1, Vector2 pointer2) {
		// Do nothing
		return false;
	}
}
