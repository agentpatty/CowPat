package com.paterson.GameWorld;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.paterson.GameObjects.Catcher;
import com.paterson.GameObjects.Container;
import com.paterson.GameObjects.Faller;
import com.paterson.GameObjects.MoveableObject;
import com.paterson.GameObjects.Poo;
import com.paterson.GameObjects.PooDropper;
import com.paterson.GameObjects.Projectile;
import com.paterson.GameObjects.StandardCatcher;
import com.paterson.GameObjects.StandardContainer;
import com.paterson.GameObjects.Target;
import com.paterson.Helpers.AssetLoader;
import com.paterson.Helpers.IOHelper;
import com.paterson.Helpers.InputHandler;
import com.paterson.Screens.GameScreen;
import com.paterson.cowpat.CowPat;

public class GameWorld {

	public static final int MAX_LIVES = 5;
	private static final float VELOCITY_SCALE = 0.03f; // 0.05f - used earlier, may be better for android/emulator
	private static final int TARGET_HIT_BONUS = 50;
	
	public boolean isWaitingOnPicture = false;
	
	private static final int CATCHER_HEIGHT = 20;
	
	private CowPat game;
	private InputHandler iHandler;
	
	private ArrayList<Faller> fallingList;
	private Deque<Poo> scoredPoo;
	
	private PooDropper pooDropper;
	private Catcher catcher;
	private Container container;
	private Target target;
	
	private MoveableObject throwingContainer;
	private Projectile pooProjectile;
	
	private int score = 0;
	private int gameHeight;
	private int groundLocation;
	
	private GameLevel currentLevel;
	
	private int numLives;
	private Boolean isFallingGame;
	private GameState currentState;
	private GameState prePauseGameState;
	private GameState preSettingsGameState;
	
	private int multiplier = 1;
	private int numInRow = 0;

	private Boolean menuMusicStarted = false;
	
	public enum GameState {
		MAIN_MENU, SETTINGS_MENU, ABOUT_MENU,
		READY, RUNNING, GAMEOVER, HIGHSCORE, LEVEL_COMPLETE, GAME_PAUSED,
		THROW_READY, THROW_RUNNING, THROW_COMPLETE
	}

	/**
	 * Constructor for the GameWorld - sets the initial state, and the first level. 
	 * @param game The running game
	 * @param gameHeight The height of the screen
	 */
	public GameWorld(CowPat game, int gameHeight) {
		this.game = game;
		this.currentState = GameState.MAIN_MENU;
		this.currentLevel = GameLevel.getFirstLevel(this);
		this.isFallingGame = true;
		this.gameHeight = gameHeight;
		this.groundLocation = gameHeight - 11;
		this.pooDropper = this.currentLevel.getPooDropper();
		this.fallingList = new ArrayList<Faller>();
		this.scoredPoo = new ArrayDeque<Poo>();
		this.catcher = new StandardCatcher(0, this.groundLocation - CATCHER_HEIGHT + 4, 30, CATCHER_HEIGHT, this.currentLevel.getCatcherSpeed());
		this.container = new StandardContainer(15, 11, this.currentLevel.getContainerCapacity(), this.catcher);
		this.numLives = GameWorld.MAX_LIVES;
		this.target = new Target(0,(gameHeight/2) - 30,60,60);
		this.throwingContainer = new MoveableObject(0, this.groundLocation - 2, 30,50);
		this.pooProjectile = new Projectile(5, 3);
	}

	/**
	 * Main update method, which triggers the required updates based on the current state of the game
	 * @param delta time since last update
	 */
	public void update(float delta) {
		//runTime += delta;
		switch (currentState) {
		case MAIN_MENU:
			if (!menuMusicStarted)
			{
				// First time through, start the music (if it is on)
				menuMusicStarted = true;
				if (AssetLoader.getMusicSetting())
				{
					AssetLoader.menuMusic.play();
				}
			}
			// Update running to have the game running continuously in the background
			updateRunning(delta);
			break;
		case SETTINGS_MENU:
			//Update running to have the game running continuously in the background
			if (this.preSettingsGameState == GameState.MAIN_MENU)
			{
				// Only update if we came from main menu, otherwise we came from paused menu
				updateRunning(delta);
			}
			break;
		case ABOUT_MENU:
			//Update running to have the game running continuously in the background
			updateRunning(delta);
			break;
		case READY:
			break;
		case THROW_READY:
			updateTarget(delta);
			break;
		case RUNNING:
			updateRunning(delta);
			break;
		case THROW_RUNNING:
			updateThrowingRunning(delta);
			break;
		case GAMEOVER:
			break;
		case HIGHSCORE:
			break;
		case LEVEL_COMPLETE:
			break;
		case THROW_COMPLETE:
			break;
		default:
			break;
		}
	}

	/**
	 * This method moves the game to the next level and resets the required fields ready to start a new level
	 */
	private void prepareLevel() {
		// Moves to the next level
		currentLevel.nextLevel();
		pooDropper.reset();
		pooDropper.setFallFrequency(currentLevel.getTimeBetweenFall());
		container.reset();
		container.setCapacity(currentLevel.getContainerCapacity());
		catcher.setSpeed(currentLevel.getCatcherSpeed());
		fallingList.clear();
		pooProjectile.reset();
	}

	/**
	 * Main update of the catching game. Where the logic is held
	 * @param delta the time since method was last called
	 */
	public void updateRunning(float delta) {
		if (delta > .15f) {
			delta = .15f;
		}
		pooDropper.update(delta);
		
		// Switch direction once the catcher hits the edge
		if (catcher.getX() + catcher.getWidth() >= this.getGameWidth() && catcher.isMovingRight())
		{
			catcher.setMovingLeft();
		}
		else if (catcher.getX() <= 0  && !catcher.isMovingRight())
		{
			catcher.setMovingRight();
		}
		
		catcher.update(delta);
		container.update(delta);
		
		// check all the fallers for updates
		for (Faller faller : fallingList)
		{
			if (faller.hitGround())
			{
				// Record the item has fallen just once so we don't take off more than 1 life
				if (!faller.hasRecordedFallen())
				{
					if (this.isRunning())
					{
						numLives--;
					}
					faller.recordFallen();
					this.numInRow = 0;
					this.multiplier = 1;
					if (AssetLoader.getSoundsSetting() && this.isRunning())
					{
						AssetLoader.pooLand.play();
					}
				}
			}
			else {
				// If the faller has scored then it is hidden and there is nothing to do with it
				if (!faller.hasScored())
				{
					faller.update(delta);
					// Check to see if the faller has entered the container - do a quick check of the Y value for performance first
					if (faller.getY() + 5 > container.getY() && Intersector.overlaps(container.getContainerOpening(), faller.getCollisionArea()))
					{
						// Only add the score if the game is running (otherwise we will be on the menu where we want it to run continuously)
						if (this.isRunning())
						{
							this.addScore(faller.getScoringValue());
							container.fillContainer((int)faller.getWidth());
						}
						faller.setScored();
						if (AssetLoader.getSoundsSetting() && this.isRunning())
						{
							AssetLoader.pooCaught.play();
						}
						this.numInRow++;
						// Each 5 in a row increase the score multiplier
						if (this.numInRow % 5 == 0)
						{
							this.multiplier++;
						}
						// If you catch 10 in a row - get a new life
						if (this.numInRow % 10 == 0)
						{
							this.numLives++;
						}
						// Add to a stack to re-use object later
						if (faller instanceof Poo)
						{
							scoredPoo.push((Poo)faller);
						}
					}
				}
			}
		}
		
		// Check game state to see if we have run out of lives or completed the level
		if (numLives <= 0)
		{
			// Game over
			currentState = GameState.GAMEOVER;
			if (AssetLoader.getMusicSetting())
			{
				AssetLoader.gameMusic.stop();
				AssetLoader.gameOver.play();
			}
			if (score > AssetLoader.getHighScore())
			{
				this.setHighScore(score);
				currentState= GameState.HIGHSCORE;
			}
		}
		else if (container.isContainerFull())
		{
			currentState = GameState.LEVEL_COMPLETE;
		}
		
	}
	
	/**
	 * Update the running of the throwing of the poo game
	 * @param delta The time between the last call to this procedure
	 */
	private void updateThrowingRunning(float delta)
	{
		updateTarget(delta);
		updateProjectile(delta);
	}
	
	/**
	 * Call the update of the target
	 * @param delta time since last update
	 */
	private void updateTarget(float delta)
	{
		// Make the target move side to side - as they hit the edge of the screen change direction
		if (target.getX() + target.getWidth() >= this.getGameWidth() && target.isMovingRight())
		{
			target.setMovingLeft();
		}
		else if (target.getX() <= 0  && !target.isMovingRight())
		{
			target.setMovingRight();
		}
		this.target.update(delta);
	}
	
	/**
	 * Call the update of the projectile to reposition the object. Stops if it hits the target and assigns points
	 * @param delta time since last call
	 */
	private void updateProjectile(float delta)
	{
		// Only need to update if the projectile is moving
		if (this.pooProjectile.getIsMoving())
		{
			// If the projectile has reached the target then stop
			if (pooProjectile.getY() >= (this.target.getY() - (this.target.getHeight()/2)))
			{
				this.pooProjectile.stopMoving();
				if (AssetLoader.getSoundsSetting())
				{
					AssetLoader.targetHit.play();
				}
				if (AssetLoader.getMusicSetting())
				{
					AssetLoader.throwingMusic.stop();
				}
				this.currentState = GameState.THROW_COMPLETE;
				// Bonus points for hitting the target
				if (pooProjectile.hitTarget(target))
				{
					this.addScore(TARGET_HIT_BONUS); 
				}
			}
			this.pooProjectile.update(delta);
		}
	}
	
	
	public void setInputHandler(InputHandler iHandler)
	{
		this.iHandler = iHandler;
	}
	
	public InputHandler getInputHandler()
	{
		return this.iHandler;
	}
	
	public PooDropper getPooDropper()
	{
		return pooDropper;
	}
	
	/**
	 * All of the fallers that have fallen so far in the level, including scored ones
	 * @return List of fallers that have started or finished falling
	 */
	public ArrayList<Faller> getFallingList()
	{
		return fallingList;
	}

	public Catcher getCatcher()
	{
		return catcher;
	}

	public Target getTarget() {
		return this.target;
	}
	
	public Projectile getProjectile() {
		return this.pooProjectile;
	}
	
	public int getGameHeight() {
		return gameHeight;
	}
	
	public int getGameWidth() {
		return GameScreen.GAME_WIDTH;
	}

	public int getScore() {
		return score;
	}

	public Container getContainer() {
		return container;
	}
	
	public int getNumberOfLives()
	{
		return this.numLives;
	}

	/**
	 * Adds to the total score by the passed in increment multiplied by the multiplier
	 * @param increment The base scored amount
	 */
	public void addScore(int increment) {
		score += (increment * this.multiplier);
	}

	/**
	 * Updates the state of the game for it to begin
	 */
	public void start() {
		this.currentState = GameState.RUNNING;
	}

	/**
	 * Gets the game ready to play the catching/falling game.
	 */
	public void ready() {
		this.isFallingGame = true;
		this.prepareLevel();
		this.currentState = GameState.READY;
		if (AssetLoader.getMusicSetting())
		{
			AssetLoader.menuMusic.stop();
			AssetLoader.gameMusic.play();
		}
	}
	
	/**
	 * Moves the game into the settings menu state
	 */
	public void goToSettings() {
		this.preSettingsGameState = this.currentState;
		this.currentState = GameState.SETTINGS_MENU;
	}
	
	public void goToAbout() {
		this.currentState = GameState.ABOUT_MENU;
	}
	
	public void goToMainMenu() {
		this.currentState = GameState.MAIN_MENU;
		if (AssetLoader.getMusicSetting())
		{
			AssetLoader.gameMusic.stop();
			AssetLoader.menuMusic.play();
		}
	}
	
	public void prepareThrowing()
	{
		this.isFallingGame = false;
		this.currentState = GameState.THROW_READY;
		if (AssetLoader.getMusicSetting())
		{
			AssetLoader.gameMusic.stop();
			AssetLoader.throwingMusic.play();
		}
	}
	
	public void startThrowing()
	{
		this.currentState = GameState.THROW_RUNNING;
	}
	
	public void pauseGame()
	{
		// Keep track of the current state so we can return to it
		this.prePauseGameState = currentState;
		this.currentState = GameState.GAME_PAUSED;
	}
	
	public void unPause()
	{
		this.currentState = this.prePauseGameState;		
	}

	public void restartGame() {
		this.resetGame();
		this.ready();
	}
	
	public void resetGame() {
		// Reset to first level
		this.currentLevel.reset();
		this.score = 0;
		this.numLives = MAX_LIVES;
		// restart objects
		this.pooDropper.reset();
		this.container.reset();
		this.fallingList.clear();
		this.isFallingGame = true;
	}

	public Boolean isFallingGame()
	{
		return this.isFallingGame;
	}
	
	/**
	 * Catch all method for all menus for convenience
	 * @return True if the game is currently running one of the menus
	 */
	public Boolean isMenu()
	{
		return this.currentState == GameState.MAIN_MENU || currentState == GameState.SETTINGS_MENU || currentState == GameState.ABOUT_MENU;
	}
	
	public Boolean isMainMenu () {
		return this.currentState == GameState.MAIN_MENU;
	}
	
	public Boolean isSettingsMenu() {
		return this.currentState == GameState.SETTINGS_MENU;
	}
	
	public Boolean isAboutMenu() {
		return this.currentState == GameState.ABOUT_MENU;
	}
	
	public Boolean isReady() {
		return this.currentState == GameState.READY;
	}

	public Boolean isGameOver() {
		return this.currentState == GameState.GAMEOVER;
	}
	
	public Boolean isGamePaused() 
	{
		return this.currentState == GameState.GAME_PAUSED;
	}

	public boolean isHighScore() {
		return this.currentState == GameState.HIGHSCORE;
	}
	
	public Boolean isRunning() {
		return this.currentState == GameState.RUNNING;
	}

	public Boolean isLevelComplete() {
		return this.currentState == GameState.LEVEL_COMPLETE;
	}
	
	public Boolean isThrowingReady() 
	{
		return this.currentState == GameState.THROW_READY;
	}
	
	public Boolean isThrowingRunning()
	{
		return this.currentState == GameState.THROW_RUNNING;
	}
	
	public Boolean isThrowingComplete()
	{
		return this.currentState == GameState.THROW_COMPLETE;
	}
	/**
	 * Returns a new Poo object that can be used
	 * @return Poo object to use
	 */
	private Poo createNewPoo(int size)
	{
		return new Poo(pooDropper,size, size -1, groundLocation, currentLevel.getFallSpeed()); 
	}
	
	/**
	 * Tries to reuse a previously used Poo object, otherwise creates a new one
	 */
	public void dropPoo()
	{
		int size = MathUtils.random(Faller.MIN_POO_WIDTH, Faller.MAX_POO_WIDTH);
		if (scoredPoo.isEmpty())
		{
			fallingList.add(createNewPoo(size));
		}
		else if (fallingList.size() > 20)
		{
			// Will only get here on the menu screen. Prevent creating infinite new objects be reusing fallen ones
			fallingList.get(MathUtils.random(0,20)).reset(size, currentLevel.getFallSpeed());
		}
		else 
		{
			Poo newPoo = scoredPoo.pop();
			newPoo.reset(size, currentLevel.getFallSpeed());
			// On a restart the scored poo may not be in the falling list
			if (!fallingList.contains(newPoo))
			{
				fallingList.add(newPoo);
			}
		}
		if (AssetLoader.getSoundsSetting() && this.isRunning())
		{
			AssetLoader.pooDrop.play();
		}
	}
	
	public void moveRight()
	{
		if (!this.catcher.isMovingRight())
		{
			this.catcher.setMovingRight();
		}
	}
	
	public void moveLeft()
	{
		if (this.catcher.isMovingRight())
		{
			this.catcher.setMovingLeft();
		}
	}

	/**
	 * Get a new picture from the system
	 */
	public void selectNewTarget()
	{
		// Ask the platform dependent file chooser to find a file
		if (this.game.getPictureChooser() != null)
		{
			System.out.println("Getting new picture path.");
			this.isWaitingOnPicture = true;
			this.game.getPictureChooser().selectPicture();
			System.out.println("Finished call to get new path.");
			// This runs in a different activity so we handle the result in a different place
		}
		else 
		{
			IOHelper.getNewTarget();
		}
	}
	
	public void setNextTarget()
	{
		AssetLoader.moveToNextTarget();
	}
	
	public void setPreviousTarget()
	{
		AssetLoader.moveToPreviousTarget();
	}
	
	/**
	 * Set a new high score
	 * @param score The score to set as the new high score.
	 */
	public void setHighScore(int score)
	{
		AssetLoader.setHighScore(score);
	}

	public int getLevel() {
		return this.currentLevel.getLevelNumber();
	}
	
	public int getNumberInRow()
	{
		return this.numInRow;
	}
	
	/**
	 * Get the multiplier that is being used for every poo that is caught. Increases every 5 catches in a row
	 * @return Multiplier applied to each poo when it is caught.
	 */
	public int getScoreMultiplier()
	{
		return this.multiplier;
	}
	
	public MoveableObject getThrowingContainer()
	{
		return this.throwingContainer;
	}
	
	public void setThrowingContainerX(int newX)
	{
		// Adjust so that the touched screen location is the centre of the container
		float screenX = newX - this.throwingContainer.getWidth()/2;
		// Make sure that the container doesn't go off the screen
		if (screenX < 0)
		{
			screenX = 0;
		}
		else if (screenX + this.throwingContainer.getWidth() > this.getGameWidth())
		{
			screenX = this.getGameWidth() - this.throwingContainer.getWidth();
		}
		// Now update the x position of the container
		this.throwingContainer.setXPosition(screenX);
	}
	
	
	/**
	 * Starts the poo being thrown at the target
	 * @param velocityX speed of swipe across
	 * @param velocityY speed of swipe up
	 */
	public void throwPooAtTarget(float velocityX, float velocityY)
	{
		if (!pooProjectile.getIsMoving())
		{
			this.pooProjectile.setVelocity(velocityX * VELOCITY_SCALE, velocityY * VELOCITY_SCALE);
			this.pooProjectile.startMoving(this.throwingContainer.getX() + (this.throwingContainer.getWidth()/2), this.throwingContainer.getY());
		}
	}
	
	/**
	 * Closes the application
	 */
	public void exitGame()
	{
		Gdx.app.exit();
	}

	/**
	 * Returns to the screen that was previous to the settings menu - either the paused menu or the main menu
	 */
	public void goToBackFromSettingsMenu() {
		this.currentState = this.preSettingsGameState;
	}
	
	/**
	 * Procedure for cheating during testing and instantly fill the container
	 */
	public void levelSkip()
	{
		if (this.isRunning())
		{
			this.container.fillContainer(currentLevel.getContainerCapacity());
		}
	}
}

