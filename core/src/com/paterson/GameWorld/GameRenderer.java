package com.paterson.GameWorld;

import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.paterson.GameObjects.Catcher;
import com.paterson.GameObjects.Container;
import com.paterson.GameObjects.Faller;
import com.paterson.GameObjects.MoveableObject;
import com.paterson.GameObjects.PooDropper;
import com.paterson.GameObjects.Projectile;
import com.paterson.GameObjects.Target;
import com.paterson.Helpers.AssetLoader;
import com.paterson.Helpers.InputHandler;
import com.paterson.TweenAccessors.Value;
import com.paterson.TweenAccessors.ValueAccessor;
import com.paterson.UI.ClickableObject;

public class GameRenderer {

	public static final String KEY_BACK = "back";
	public static final String KEY_BACK_TO_MENU = "backToMenu";
	public static final String KEY_BUCKET_FULL = "bucketFull";
	public static final String KEY_CLEAR_HIGH_SCORE = "clearHighScore";
	public static final String KEY_FLING_IT = "flingIt";
	public static final String KEY_GAME_OVER = "gameOver";
	public static final String KEY_GREAT_THROW = "greatThrow";
	public static final String KEY_HIGH_SCORE = "highScore";
	public static final String KEY_HOW_TO_PLAY = "howToPlay";
	public static final String KEY_IN_A_ROW = "inARow";
	public static final String KEY_INSTRUCTIONS = "instructions";
	public static final String KEY_JUST_GOT_EM = "justGotEm";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_LEVEL_COMPLETE = "levelComplete";
	public static final String KEY_MULTIPLIER = "multiplier";
	public static final String KEY_MUSIC = "music";
	public static final String KEY_NEW_GAME = "newGame";
	public static final String KEY_NEW_HIGH_SCORE = "newHighScore";
	public static final String KEY_PAUSE = "pause";
	public static final String KEY_PAUSED = "paused";
	public static final String KEY_PRESS_TO_START = "pressToStart";
	public static final String KEY_QUIT = "quit";
	public static final String KEY_RESUME = "resume";
	public static final String KEY_SCORE = "score";
	public static final String KEY_SETTINGS = "settings";
	public static final String KEY_SOUND = "sound";
	public static final String KEY_TARGET_PIC = "targetPic";
	public static final String KEY_TRY_AGAIN = "tryAgain";
	public static final String KEY_WASTE = "waste";

	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private Integer gameWidth;
	private Integer gameHeight;
	private int backButtonY;
	private int backButtonX;
	private int headingY;

	private SpriteBatch batcher;

	private int midPointY;

	// Game Objects
	private PooDropper pooDropper;
	private ArrayList<Faller> fallingList;
	private Catcher catcher;
	private Container container;
	private Target target;
	private MoveableObject throwingContainer;
	private Projectile pooProjectile;

	// Game Assets
	private TextureRegion background, pooImage, catcherLeft, catcherRight, bucketImage, fullBucketImage, pooSplat;
	private Animation cowAnimation, stinkAnimation;
	private BitmapFont font, shadowFont;

	// Tween stuff
	private TweenManager manager;
	private Value alpha = new Value();

	// Buttons
	private ClickableObject newGameButton, settingsButton, aboutButton, soundCheckbox, musicCheckbox, backButton, /** targetButton,**/ clearHighScoreButton,
			tryAgainButton, returnToMenuButton, /**exitButton,**/
			pausedResumeButton, pausedExitButton, pausedSettingsButton, pauseButton, leftArrowButton, rightArrowButton;

	private Color transitionColor;



	public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
		myWorld = world;
		this.gameWidth = world.getGameWidth();
		this.gameHeight = gameHeight;

		this.midPointY = midPointY;
		this.backButtonY = gameHeight - 25;
		this.backButtonX = 10;
		this.headingY = midPointY - 40;

		cam = new OrthographicCamera();
		cam.setToOrtho(true, this.gameWidth, gameHeight);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		initGameObjects();
		initAssets();

		// Used to transition between screens (e.g. loading screen and game screen)
		transitionColor = new Color();
		prepareTransition(255, 255, 255, .5f);
	}

	/**
	 * Gets all of the game objects in memory ready to be used to render the game
	 */
	private void initGameObjects() {
		pooDropper = myWorld.getPooDropper();
		fallingList = myWorld.getFallingList();
		catcher = myWorld.getCatcher();
		container = myWorld.getContainer();
		target = myWorld.getTarget();
		throwingContainer = myWorld.getThrowingContainer();
		pooProjectile = myWorld.getProjectile();
	}

	/**
	 * Loads all of the images and sounds from the AssetLoader so they are held in memory in this class too
	 */
	private void initAssets() {
		background = AssetLoader.background;
		font = AssetLoader.font;
        shadowFont = AssetLoader.shadowFont;
		cowAnimation = AssetLoader.cowAnimation;
		pooImage = AssetLoader.poo;
		catcherLeft  = AssetLoader.catcherLeft;
		catcherRight = AssetLoader.catcherRight;
		bucketImage = AssetLoader.bucket;
		fullBucketImage = AssetLoader.fullBucket;
		pooSplat = AssetLoader.pooSplat;
		stinkAnimation = AssetLoader.stinkAnimation;

		InputHandler input = myWorld.getInputHandler();
		aboutButton = input.getAboutButton();
		backButton = input.getBackButton();
		musicCheckbox = input.getMusicCheckbox();
		newGameButton = input.getNewGameButton();
//		exitButton = input.getExitButton();
		soundCheckbox = input.getSoundCheckbox();
		settingsButton = input.getSettingsButton();
		clearHighScoreButton = input.getClearHighScoreButton();
		pausedExitButton = input.getPausedExitButton();
		pausedResumeButton = input.getPausedResumeButton();
		pausedSettingsButton = input.getpausedSettingsButton();
//		targetButton = input.getTargetButton(); Not able to select new targets for now
		pauseButton = input.getPauseButton();
		tryAgainButton = input.getTryAgainButton();
		returnToMenuButton = input.getReturnToMenuButton();
		leftArrowButton = input.getLeftArrowButton();
		rightArrowButton = input.getRightArrowButton();
	}

	/**
	 * Sets the font size to be extra large - used for the main game title
	 */
	public void setTitleFontSize()
	{
		font.getData().setScale(.5f, -.5f);
        shadowFont.getData().setScale(.5f, -.5f);
	}

	/**
	 * Sets the font size to be large - used for key information (e.g. Game over)
	 */
	public void setLargeFontSize()
	{
		font.getData().setScale(.25f, -.25f);
        shadowFont.getData().setScale(.25f, -.25f);
	}

	/**
	 * Set a medium font size - used for sentences and less important information
	 */
	public void setMidFontSize()
	{
		font.getData().setScale(.18f, -.18f);
        shadowFont.getData().setScale(.18f, -.18f);
	}

	/**
	 * Set a small font size - used for game status (e.g. score, fill %)
	 */
	public void setSmallFontSize()
	{
		font.getData().setScale(.10f, -.10f);
	}

	/**
	 * Main rendering method that calls each draw method as appropriate
	 * @param delta The time since this method was last called. Used to scale the movements based on the frame rate
	 * @param runTime The total time the application has been running. Used to determine which animation to display in the sequence
	 */
	public void render(float delta, float runTime) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batcher.begin();
		// No transparency
		batcher.disableBlending();
		batcher.draw(background, 0, 0, this.gameWidth, this.gameHeight);
		batcher.end();

		// Determine if we should show the current score, lives and how full the container is
		Boolean drawGameStatus = !myWorld.isMainMenu() && !myWorld.isSettingsMenu() && !myWorld.isAboutMenu();

		if (myWorld.isFallingGame())
		{
			if (drawGameStatus)
			{
				// TODO change the fill meter to also use batcher rather than shape renderer so this can be moved
				this.drawFillMeter();
				batcher.begin();
				batcher.enableBlending();
				this.drawPauseButton();
			}
			if (!batcher.isDrawing())
			{
				batcher.begin();
				batcher.enableBlending();
			}

			this.drawCow(runTime);
			this.drawCatcher();
			this.drawContainer(runTime);
			this.drawFallers();

			if (drawGameStatus)
			{
				this.drawLives();
				this.drawFillMeterPercent();
			}

		}
		else
		{
			batcher.begin();
			// Transparency
			batcher.enableBlending();
			this.drawTarget();
			this.drawThrowingContainer();
			this.drawPooProjectile();
			this.drawPauseButton();
			this.drawLives();
		}


		if (myWorld.isMainMenu()) {
			drawMainMenu(runTime);
		} else if (myWorld.isSettingsMenu()) {
			drawSettingsMenu();
		} else if (myWorld.isAboutMenu()) {
			drawAboutMenu();
		} else if (myWorld.isReady()) {
			drawReady();
		} else if (myWorld.isThrowingReady()) {
			drawThrowingReady();
		} else if (myWorld.isGameOver()) {
			drawGameOver();
		} else if (myWorld.isHighScore()) {
			drawHighScore();
		} else if (myWorld.isLevelComplete()) {
			drawLevelComplete();
		} else if (myWorld.isThrowingComplete()) {
			drawThrowingComplete();
		} else if (myWorld.isGamePaused()) {
			drawPaused();
		}

		batcher.end();
		// If we are transitioning between screens, draw the transition
		drawTransition(delta);

	}

	/**
	 * Draws the transition between the inital loading screen and the main menu
	 * @param delta
	 */
	private void drawTransition(float delta) {
		if (alpha.getValue() > 0) {
			manager.update(delta);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(transitionColor.r, transitionColor.g,
					transitionColor.b, alpha.getValue());
			shapeRenderer.rect(0, 0, this.gameWidth, 300);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}

	/**
	 * Draws the high score, the main title and all the buttons for the menu
	 */
	private void drawMainMenu(float runTime) {
		// First draw the high score in the top left
		AssetLoader.easyFont.draw(batcher, AssetLoader.translations.get(GameRenderer.KEY_HIGH_SCORE) +  ": " + AssetLoader.getHighScore(), 2, 2);
		// Draw the main title
		this.setTitleFontSize();
		font.draw(batcher,"COWPAT", 13, midPointY/4 );

		batcher.draw(cowAnimation.getKeyFrame(runTime), 45, midPointY/2 + 3, 50, 50);

		// Draw the buttons
		this.setLargeFontSize();
		newGameButton.draw(batcher, 29,midPointY);
		aboutButton.draw(batcher, 19, midPointY + 20);
		settingsButton.draw(batcher, 36, midPointY + 40);
//		exitButton.draw(batcher, backButtonX, backButtonY);
	}

	private void drawSettingsMenu() {
		this.setMidFontSize();
		float yStart = 10;
		// Music
		font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_MUSIC), 10, yStart);
		musicCheckbox.draw(batcher, 110, yStart-2);
		// Sounds
		font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_SOUND), 10, yStart +20);
		soundCheckbox.draw(batcher, 110, yStart +18);
		// Picture
		font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_TARGET_PIC), 40, yStart +40);
		//targetButton.draw(batcher, 10,yStart +125);
		leftArrowButton.draw(batcher, 10, yStart + 75);
		rightArrowButton.draw(batcher, 115, yStart + 75);

		batcher.draw(AssetLoader.target.getTexture(), 40, yStart +55, 0, 0, 60, 60, 1, 1, 0, 0, 0, AssetLoader.target.getTexture().getWidth(), AssetLoader.target.getTexture().getHeight(), false, true);
		//batcher.draw(AssetLoader.target.getTexture(), 40, yStart +55, 60,60);
		clearHighScoreButton.draw(batcher, 20, yStart + 140);

		this.setLargeFontSize();
		backButton.draw(batcher, backButtonX, backButtonY);
	}

	/**
	 * Draws the screen after clicking the About button. Explains how to play the game.
	 */
	private void drawAboutMenu() {
		this.setLargeFontSize();
		font.draw(batcher, AssetLoader.translations.get(GameRenderer.KEY_HOW_TO_PLAY), 10, 10);
		this.setSmallFontSize();
//		AssetLoader.easyFont.draw(batcher, "Fill the bucket and throw it\n" +
//				"all over your target!\n\nPress the right or left of the\nscreen to move the catcher\nunder the poo - he will\ncontinue that direction\n" +
//				"until the edge of the screen.\n\n3-5 points for every catch\n50 points for hitting your target\nEach is multiplied by the\n" +
//				"mulitplier. This increases\nwith every 5 catches in a row.\n10 in a row gets a new life.", 5, 25);
		AssetLoader.easyFont.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_INSTRUCTIONS),5,25);
		this.setLargeFontSize();
		backButton.draw(batcher, backButtonX, backButtonY);
	}

	/**
	 * Draw the text displayed when the user is ready to begin each catching game level
	 */
	private void drawReady() {
		this.setLargeFontSize();
		font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_LEVEL)+ " " + myWorld.getLevel(), 40, midPointY - 20);
		AssetLoader.easyFont.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_PRESS_TO_START), 40, midPointY);
	}

	/**
	 * Draw the text displayed when the user has successfully passed the catch game level
	 */
	private void drawLevelComplete()
	{
		this.setLargeFontSize();
		font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_BUCKET_FULL), 15, midPointY - 20);
		AssetLoader.easyFont.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_LEVEL_COMPLETE), 35, midPointY);
	}

	/**
	 * Draw the paused menu for the user to either resume, exit or change the settings.
	 */
	private void drawPaused()
	{
		this.setLargeFontSize();
		font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_PAUSED), 42, 45);
		this.setMidFontSize();
		// Draw resume, exit, and settings. Change the picture half way through?
		pausedResumeButton.draw(batcher, 45, midPointY-20);
		pausedSettingsButton.draw(batcher, 43, midPointY -6);
		pausedExitButton.draw(batcher, 49, midPointY+28);
	}

	/**
	 * If the user has lost all of their lives then this is run.
	 */
	private void drawGameOver() {
		this.setLargeFontSize();
		font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_GAME_OVER), 30, headingY);
		this.setMidFontSize();
		tryAgainButton.draw(batcher, 40, midPointY + 20);
		returnToMenuButton.draw(batcher, 30, midPointY+40);
	}

	/**
	 * If the user got a new high score, display this text
	 */
	private void drawHighScore()
	{
		this.setLargeFontSize();
		font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_NEW_HIGH_SCORE), 5, headingY);
		AssetLoader.easyFont.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_SCORE) + ": " + myWorld.getScore(), 40, midPointY - 20);
		this.setMidFontSize();
		tryAgainButton.draw(batcher, 40, midPointY + 20);
		returnToMenuButton.draw(batcher, 30, midPointY+40);
	}

	private void drawThrowingReady() {
		this.setLargeFontSize();
		font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_FLING_IT), 15, headingY);
		AssetLoader.easyFont.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_PRESS_TO_START), 40, midPointY);
		// TODO add animation to show the user they need to flick/fling to fire the poo
	}

	/**
	 * Render the screen displayed once the poo has been thrown at the target - will change based on how good the throw was
	 */
	private void drawThrowingComplete()
	{
		batcher.draw(pooSplat, pooProjectile.getX()- (target.getWidth()/2), target.getY(), target.getWidth(), target.getHeight());
		this.setLargeFontSize();
		if (pooProjectile.hitTarget(target))
		{
			font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_GREAT_THROW), 15, headingY);
		}
		else if (pooProjectile.hitTargetSlightly(target))
		{
			font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_JUST_GOT_EM), 15, headingY);
		}
		else
		{
			font.draw(batcher,AssetLoader.translations.get(GameRenderer.KEY_WASTE), 13, headingY);
		}
	}

	public void prepareTransition(int r, int g, int b, float duration) {
		transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
		alpha.setValue(1);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		Tween.to(alpha, -1, duration).target(0)
				.ease(TweenEquations.easeOutQuad).start(manager);
	}

	/**
	 * Draws the cow that is dropping the poo
	 * @param runTime total time the application has been running. Used for the animation to know which frame to display
	 */
	private void drawCow(float runTime)
	{
		batcher.draw(cowAnimation.getKeyFrame(runTime), pooDropper.getX(),pooDropper.getY(), pooDropper.getWidth(), pooDropper.getHeight());
	}

	/**
	 * Draws all of the falling objects (including those that have hit the ground)
	 */
	private void drawFallers()
	{
		for (Faller faller: fallingList)
		{
			// Only draw if it is on the screen and hasn't been caught (i.e. scored).
			if (!faller.hasScored() && faller.getX() > 0 && faller.getX() < this.gameWidth)
			{
				batcher.draw(pooImage, faller.getX(),faller.getY(), faller.getWidth(), faller.getHeight());
			}
		}
	}

	/**
	 * Draws the catcher object which the user is controlling
	 */
	private void drawCatcher()
	{
		TextureRegion imageToDraw = null;
		if (catcher.isMovingRight())
		{
			imageToDraw = catcherRight;
		}
		else
		{
			imageToDraw = catcherLeft;
		}
		batcher.draw(imageToDraw, catcher.getX(),catcher.getY(), catcher.getWidth(), catcher.getHeight());
	}

	private void drawContainer(float runTime)
	{
		batcher.draw(bucketImage, container.getX(), container.getY(), 15, 12);
		if (container.isContainerFull())
		{
			batcher.draw(fullBucketImage, container.getX(), container.getY() - 3, 14, 5);
			batcher.draw(stinkAnimation.getKeyFrame(runTime), container.getX() + 4, container.getY() - 13, 3, 10);
			batcher.draw(stinkAnimation.getKeyFrame(runTime), container.getX() + 8, container.getY() - 13, 3, 10);
		}
	}

	/**
	 * Draws the indicators that show how many lives the user has left
	 */
	private void drawLives()
	{
		batcher.draw(catcherRight, 2, 1, 10, 12);
		this.setMidFontSize();
		font.draw(batcher, "x" + myWorld.getNumberOfLives(), 14, 4);
		this.setSmallFontSize();
		AssetLoader.easyFont.draw(batcher, AssetLoader.translations.get(GameRenderer.KEY_SCORE) + ": " + myWorld.getScore(), 2, 15);
		AssetLoader.easyFont.draw(batcher, AssetLoader.translations.get(GameRenderer.KEY_IN_A_ROW) + ": " + myWorld.getNumberInRow(), 2, 23);
		AssetLoader.easyFont.draw(batcher, AssetLoader.translations.get(GameRenderer.KEY_MULTIPLIER) + ": " + myWorld.getScoreMultiplier(), 2, 31);
	}

	/**
	 * Draws the meter that shows how full the container is
	 */
	private void drawFillMeter()
	{
		shapeRenderer.begin(ShapeType.Line);

		// Draw Background color
		shapeRenderer.setColor(209 / 255.0f, 146 / 255.0f, 29 / 255.0f, 1);
		shapeRenderer.rect(131, 21, 4, 50);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(209 / 255.0f, 146 / 255.0f, 29 / 255.0f, 1);

		// Find the height to fill the bar up to
		float height = 50F * this.container.getPercentFull();
		shapeRenderer.rect(131, 21 + (50 - height), 4, height);

		shapeRenderer.end();
	}

	private void drawFillMeterPercent()
	{
		this.setSmallFontSize();
		// Find the height to fill the bar up to
		float yPosition = 69 - (50F * this.container.getPercentFull());
		String text = "" + (int)(this.container.getPercentFull() * 100) + "%";
		font.draw(batcher, text, 115, yPosition);
	}

	public void drawPauseButton(){
		pauseButton.draw(batcher, this.gameWidth - (1 + pauseButton.getWidth()), 1);
	}

	/**
	 * Draws the target to throw the poo at
	 */
	private void drawTarget()
	{
		if (!this.myWorld.isFallingGame() && (this.myWorld.isGamePaused()) || this.myWorld.isSettingsMenu())
		{
			// Draw the image slightly transparent on the paused menu
			batcher.setColor(1, 1, 1, 0.5f);
		}
		batcher.draw(AssetLoader.target.getTexture(), target.getX(), target.getY(), 0, 0, target.getWidth(), target.getHeight(), 1, 1, 0, 0, 0, AssetLoader.target.getTexture().getWidth(), AssetLoader.target.getTexture().getHeight(), false, true);
		batcher.setColor(1, 1, 1, 1);
	}

	private void drawThrowingContainer() {
		batcher.draw(bucketImage, throwingContainer.getX(), throwingContainer.getY(), throwingContainer.getWidth(), throwingContainer.getHeight());
		// Show the bucket as full until the poo is flung
		if (myWorld.isThrowingReady() ||  (myWorld.isThrowingRunning() && !this.pooProjectile.getIsMoving()))
		{
			batcher.draw(fullBucketImage, throwingContainer.getX(), throwingContainer.getY(), 30, 5);
		}
	}

	private void drawPooProjectile()
	{
		if (pooProjectile.getIsMoving())
		{
			batcher.draw(pooImage, pooProjectile.getX(), pooProjectile.getY(), pooProjectile.getWidth(), pooProjectile.getHeight());
		}
	}

}
