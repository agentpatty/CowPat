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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.paterson.GameObjects.Catcher;
import com.paterson.GameObjects.Container;
import com.paterson.GameObjects.Faller;
import com.paterson.GameObjects.MoveableObject;
import com.paterson.GameObjects.PooDropper;
import com.paterson.GameObjects.Projectile;
import com.paterson.GameObjects.Target;
import com.paterson.Helpers.AssetLoader;
import com.paterson.Helpers.InputHandler;
import com.paterson.Screens.GameScreen;
import com.paterson.TweenAccessors.Value;
import com.paterson.TweenAccessors.ValueAccessor;
import com.paterson.UI.ClickableObject;

public class GameRenderer {

	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private Integer gameWidth;
	private Integer gameHeight;
	private int backButtonY;
	private int backButtonX;
	private int headingY;
	
	private SpriteBatch batcher;

	private int midPointY, midPointX;

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
	private BitmapFont font,smallFont, medFont, largeFont;;
	
	// Tween stuff
	private TweenManager manager;
	private Value alpha = new Value();
	
	private Stage stage	;
	
	// Buttons
	private ClickableObject newGameButton, settingsButton, aboutButton, soundCheckbox, musicCheckbox, backButton, targetButton, clearHighScoreButton,
		tryAgainButton, returnToMenuButton, exitButton,
		pausedResumeButton, pausedExitButton, pausedSettingsButton, pauseButton, leftArrowButton, rightArrowButton;

	private Color transitionColor;

	public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
		myWorld = world;
		this.gameWidth = GameScreen.GAME_WIDTH;
		this.gameHeight = gameHeight;
		
		this.midPointX = (int) this.gameWidth/2;
		this.midPointY = midPointY;
		this.backButtonY = 25;
		this.backButtonX = 10;
		this.headingY = midPointY + 40;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(true, this.gameWidth, gameHeight);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
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
		smallFont = AssetLoader.smallFont;
		medFont = AssetLoader.medFont;
		largeFont = AssetLoader.largeFont;
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
		exitButton = input.getExitButton();
		soundCheckbox = input.getSoundCheckbox();
		settingsButton = input.getSettingsButton();
		clearHighScoreButton = input.getClearHighScoreButton();
		pausedExitButton = input.getPausedExitButton();
		pausedResumeButton = input.getPausedResumeButton();
		pausedSettingsButton = input.getpausedSettingsButton();
		targetButton = input.getTargetButton();
		pauseButton = input.getPauseButton();
		tryAgainButton = input.getTryAgainButton();
		returnToMenuButton = input.getReturnToMenuButton();
		leftArrowButton = input.getLeftArrowButton();
		rightArrowButton = input.getRightArrowButton();
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
		// clear the stage each time and add the items in
		stage.clear();
		
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
		AssetLoader.easyFont.draw(batcher, "High Score: " + AssetLoader.getHighScore(), 2, 2);
		// Draw the main title
		//this.setTitleFontSize();
		font.draw(batcher,"COWPAT", 13, midPointY/4 );
		
		batcher.draw(cowAnimation.getKeyFrame(runTime), 45, midPointY/2 + 3, 50, 50);
		
		// Draw the buttons
		newGameButton.addToStage(stage, midPointX-30, midPointY);
		aboutButton.addToStage(stage, midPointX-45, midPointY - 60);
		settingsButton.addToStage(stage, midPointX-05, midPointY - 120);
		exitButton.addToStage(stage, backButtonX, backButtonY);
		stage.draw();
//		newGameButton.draw(batcher, 29,midPointY);
//		aboutButton.draw(batcher, 19, midPointY + 20);
//		settingsButton.draw(batcher, 36, midPointY + 40);
//		exitButton.draw(batcher, backButtonX, backButtonY);
	}

	private void drawSettingsMenu() {
		float yStart = 10;
		// Music
		medFont.draw(batcher,"Music", 10, yStart);
		musicCheckbox.draw(batcher, 110, yStart-2);
		// Sounds
		medFont.draw(batcher,"Sound Effects", 10, yStart +20);
		soundCheckbox.draw(batcher, 110, yStart +18);
		// Picture
		medFont.draw(batcher,"Target Pic", 40, yStart +40);
		targetButton.draw(batcher, 10,yStart +125);
		leftArrowButton.draw(batcher, 10, yStart + 75);
		rightArrowButton.draw(batcher, 115, yStart + 75);
		
		batcher.draw(AssetLoader.target.getTexture(), 40, yStart +55, 0, 0, 60, 60, 1, 1, 0, 0, 0, AssetLoader.target.getTexture().getWidth(), AssetLoader.target.getTexture().getHeight(), false, true);
		//batcher.draw(AssetLoader.target.getTexture(), 40, yStart +55, 60,60);
		clearHighScoreButton.draw(batcher, 10, yStart + 140);
		
		backButton.draw(batcher, backButtonX, backButtonY);
	}
	
	/**
	 * Draws the screen after clicking the About button. Explains how to play the game.
	 */
	private void drawAboutMenu() {
		largeFont.draw(batcher, "How to play", 10, 10);
		smallFont.draw(batcher, "Fill the bucket and throw it " +
				"all over your target!\n\nPress the right or left of the screen to move the catcher under the poo - he will\ncontinue that direction\n" + 
				"until the edge of the screen.\n\n3-5 points for every catch\n50 points for hitting your target\nEach is multiplied by the\n" + 
				"mulitplier. This increases\nwith every 5 catches in a row.\n10 in a row gets a new life.", 5, 25, 200, Align.left, true);
		
		backButton.draw(batcher, backButtonX, backButtonY);
	}
	
	/**
	 * Draw the text displayed when the user is ready to begin each catching game level
	 */
	private void drawReady() {
		largeFont.draw(batcher,"Level " + myWorld.getLevel(), 35, midPointY - 20);
		AssetLoader.easyFont.draw(batcher,"Press to start", 40, midPointY);
	}
	
	/**
	 * Draw the text displayed when the user has successfully passed the catch game level
	 */
	private void drawLevelComplete()
	{
		largeFont.draw(batcher,"Bucket Full!", 15, midPointY - 20);
		AssetLoader.easyFont.draw(batcher,"Level Complete", 35, midPointY);	
	}

	/**
	 * Draw the paused menu for the user to either resume, exit or change the settings.
	 */
	private void drawPaused()
	{
		largeFont.draw(batcher,"Paused", 42, 45);
		// Draw resume, exit, and settings. Change the picture half way through?
		pausedResumeButton.draw(batcher, 45, midPointY-20);
		pausedSettingsButton.draw(batcher, 43, midPointY -6);
		pausedExitButton.draw(batcher, 49, midPointY+28);
	}
	
	/**
	 * If the user has lost all of their lives then this is run. 
	 */
	private void drawGameOver() {
		largeFont.draw(batcher,"Game over", 30, headingY);
		tryAgainButton.draw(batcher, 40, midPointY + 20);
		returnToMenuButton.draw(batcher, 30, midPointY+40);
	}
	
	/**
	 * If the user got a new high score, display this text
	 */
	private void drawHighScore()
	{
		largeFont.draw(batcher,"New High Score!", 5, headingY);
		AssetLoader.easyFont.draw(batcher,"Score: " + myWorld.getScore(), 40, midPointY - 20);
		tryAgainButton.draw(batcher, 40, midPointY + 20);
		returnToMenuButton.draw(batcher, 30, midPointY+40);
	}
	
	private void drawThrowingReady() {
		largeFont.draw(batcher,"Now fling it!!", 15, headingY);
		AssetLoader.easyFont.draw(batcher,"Press to start", 40, midPointY);
		// TODO add animation to show the user they need to flick/fling to fire the poo
	}
	
	/**
	 * Render the screen displayed once the poo has been thrown at the target - will change based on how good the throw was
	 */
	private void drawThrowingComplete()
	{
		batcher.draw(pooSplat, pooProjectile.getX()- (target.getWidth()/2), target.getY(), target.getWidth(), target.getHeight());
		if (pooProjectile.hitTarget(target))
		{
			largeFont.draw(batcher,"Great Throw!", 15, headingY);
		}
		else if (pooProjectile.hitTargetSlightly(target))
		{
			largeFont.draw(batcher,"Just got 'em!", 15, headingY);
		}
		else
		{
			largeFont.draw(batcher,"What a waste!", 13, headingY);
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
		medFont.draw(batcher, "x" + myWorld.getNumberOfLives(), 14, 4);
		smallFont.draw(batcher, "Score: " + myWorld.getScore(), 2, 15);
		smallFont.draw(batcher, "In a Row: " + myWorld.getNumberInRow(), 2, 23);
		smallFont.draw(batcher, "Multiplier: " + myWorld.getScoreMultiplier(), 2, 31);
	}
	
	/**
	 * Draws the meter that shows how full the container is
	 */
	private void drawFillMeter()
	{
		shapeRenderer.begin(ShapeType.Line);

		// Draw Background color
		shapeRenderer.setColor(209 / 255.0f, 146 / 255.0f, 29 / 255.0f, 1);
		shapeRenderer.rect(131, 11, 4, 50);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(209 / 255.0f, 146 / 255.0f, 29 / 255.0f, 1);
		
		// Find the height to fill the bar up to
		float height = 50F * this.container.getPercentFull();
		shapeRenderer.rect(131, 11 + (50 - height), 4, height);
		
		shapeRenderer.end();
	}
	
	private void drawFillMeterPercent()
	{
		// Find the height to fill the bar up to
		float yPosition = 59 - (50F * this.container.getPercentFull());
		String text = "" + (int)(this.container.getPercentFull() * 100) + "%";
		smallFont.draw(batcher, text, 115, yPosition);
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
