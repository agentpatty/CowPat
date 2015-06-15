package com.paterson.Helpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class AssetLoader {

	public static final String LOCAL_TARGETS_FOLDER_NAME = "targets";
	public static final String EXTERNAL_TARGETS_FOLDER_NAME = "targets";
	
//	private static final String PREFS_INTERNAL_SETTING = "INTERNAL";
//	private static final String PREFS_LOCAL_SETTING = "LOCAL";
	private static final String PREFS_HIGH_SCORE = "highScore";
	private static final String PREFS_SOUND = "sound";
	private static final String PREFS_MUSIC = "music";

	private static final String PREFS_TARGET_PIC_PATH = "target";
	private static final String PREFS_TARGET_PIC_X = "targetX";
	private static final String PREFS_TARGET_PIC_Y = "targetY";
	private static final String PREFS_TARGET_PIC_WIDTH = "targetWidth";
	private static final String PREFS_TARGET_PIC_HEIGHT = "targetHeight";
	private static final String PREFS_TARGET_LIST = "";
	private static final String PREFS_TARGET_INDEX = "targetListIndex";
	
	public static Texture texture, backgroundTexture, logoTexture;
	public static ArrayList<TextureRegion> targetsList; 
	public static Integer targetIndex = 0;
	public static TextureRegion logo, splashImage, background, cow1, cow2, cow3, cow4, poo, grass, catcherLeft, catcherRight, bucket, fullBucket, cross, target, pooSplat, checkBox, pauseButton,
		stinkLine1, stinkLine2, stinkLine3, stinkLine4, stinkLine5, stinkLine6, stinkLine7, stinkLine8, stinkLine9, stinkLine10, lightningBolt, rightArrow,leftArrow;
	public static Animation cowAnimation, stinkAnimation;
	public static Sound pooDrop, pooCaught, targetHit, pooLand, gameOver; 
	public static Music menuMusic, gameMusic, throwingMusic;
	public static BitmapFont font, easyFont, smallFont, medFont, largeFont;
	private static Preferences prefs;

	public static Label.LabelStyle smallStyle, medStyle, largeStyle;
	
	public static void load() {
		// Create (or retrieve existing) preferences file
		prefs = Gdx.app.getPreferences("CowPat");
		//prefs.clear(); // For testing clear the settings every time
		//prefs.flush();
		if (!prefs.contains(PREFS_HIGH_SCORE)) {
			prefs.putInteger(PREFS_HIGH_SCORE, 0);
		}
		if (!prefs.contains(PREFS_SOUND)) {
			prefs.putBoolean(PREFS_SOUND, true);
		}
		if (!prefs.contains(PREFS_MUSIC)) {
			prefs.putBoolean(PREFS_MUSIC, true);
		}
		if (!prefs.contains(PREFS_TARGET_LIST)) {
			prefs.putString(PREFS_TARGET_LIST, "");
		}
		if (!prefs.contains(PREFS_TARGET_INDEX))
		{
			prefs.putInteger(PREFS_TARGET_INDEX, 0);
		}
		if (!prefs.contains(PREFS_TARGET_PIC_PATH)) {
			System.out.println("No target saved - Setting default target");
			prefs.putInteger(PREFS_TARGET_PIC_X, 0);
			prefs.putInteger(PREFS_TARGET_PIC_Y, 0);
			prefs.putInteger(PREFS_TARGET_PIC_WIDTH, 60);
			prefs.putInteger(PREFS_TARGET_PIC_HEIGHT, 60);
		}
		prefs.flush();
		
		logoTexture = new Texture(Gdx.files.internal("images/CPatSystems.png"));
		logoTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		splashImage = new TextureRegion(logoTexture);
		
		backgroundTexture = new Texture(Gdx.files.internal("images/background.png"));
		backgroundTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		background = new TextureRegion(backgroundTexture);
		background.flip(false, true);		
		
		texture = new Texture(Gdx.files.internal("images/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);	
		
		targetsList = new ArrayList<TextureRegion>();
		AssetLoader.getAllTargets();
		
		pooSplat = new TextureRegion(texture, 62, 211, 60, 57);
		pooSplat.flip(false, true);
		
		checkBox = new TextureRegion(texture, 0, 268, 40, 42);
		checkBox.flip(false, true);
		
		//splashImage = new TextureRegion(texture, 0, 0, 80, 89);
		
		cow1 = new TextureRegion(texture, 0, 0, 80, 89);
		cow2 = new TextureRegion(texture, 80, 0, 80, 89);
		cow3 = new TextureRegion(texture, 160, 0, 80, 89);
		cow4 = new TextureRegion(texture, 240, 0, 80, 89);
		cow1.flip(false, true);
		cow2.flip(false, true);
		cow3.flip(false, true);
		cow4.flip(false, true);

		TextureRegion[] cows = { cow1, cow2, cow3, cow4 };
		cowAnimation = new Animation(0.11f, cows);
		cowAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		
		poo = new TextureRegion(texture, 0,90, 30,24);
		poo.flip(false,true);
		
		lightningBolt = new TextureRegion(texture, 62,127,26,29);
		lightningBolt.flip(false,true);
	
		catcherLeft = new TextureRegion(texture, 0, 127, 57, 57);
		catcherLeft.flip(true, true);
		
		catcherRight = new TextureRegion(texture, 0, 127, 57, 57);
		catcherRight.flip(false, true);
		
		bucket = new TextureRegion(texture, 0, 188, 20, 23);
		bucket.flip(false, true);
		
		fullBucket = new TextureRegion(texture, 21, 188, 20, 5);
		fullBucket.flip(false, true);
		
		stinkLine1 = new TextureRegion(texture, 107, 189, 7, 20);
		stinkLine2 = new TextureRegion(texture, 100, 189, 7, 20);
		stinkLine3 = new TextureRegion(texture, 93, 189, 7, 20);
		stinkLine4 = new TextureRegion(texture, 86, 189, 7, 20);
		stinkLine5 = new TextureRegion(texture, 79, 189, 7, 20);
		stinkLine6 = new TextureRegion(texture, 72, 189, 7, 20);
		stinkLine7 = new TextureRegion(texture, 65, 189, 7, 20);
		stinkLine8 = new TextureRegion(texture, 58, 189, 7, 20);
		stinkLine9 = new TextureRegion(texture, 51, 189, 7, 20);
		stinkLine10 = new TextureRegion(texture, 44, 189, 7, 20);
		stinkLine1.flip(false,true);
		stinkLine2.flip(false,true);
		stinkLine3.flip(false,true);
		stinkLine4.flip(false,true);
		stinkLine5.flip(false,true);
		stinkLine6.flip(false,true);
		stinkLine7.flip(false,true);
		stinkLine8.flip(false,true);
		stinkLine9.flip(false,true);
		stinkLine10.flip(false,true);

		TextureRegion[] stinkLines = { stinkLine1, stinkLine2, stinkLine3, stinkLine4, stinkLine5, stinkLine6, stinkLine7, stinkLine8, stinkLine9, stinkLine10};
		stinkAnimation = new Animation(0.2f, stinkLines);
		stinkAnimation.setPlayMode(Animation.PlayMode.LOOP);
		
		cross = new TextureRegion(texture, 0, 212, 62, 59);
		
		pauseButton = new TextureRegion(texture, 41,271,27,39);
		pauseButton.flip(false, true);
		
		grass = new TextureRegion(texture, 0, 116, 143, 10);
		grass.flip(false, true);
		
		rightArrow = new TextureRegion(texture,69,269,16,26);
		leftArrow = new TextureRegion(texture,69,269,16,26);
		rightArrow.flip(false,true);
		leftArrow.flip(true, true);
		
		FileHandle fontFile = Gdx.files.local("fonts/WorstPaintJobEver.ttf");
		SmartFontGenerator fontGen = new SmartFontGenerator();
		Color brown = new Color(139, 69, 19, 10);
		smallFont = fontGen.createFont(fontFile, "font-18", 18);
		medFont = fontGen.createFont(fontFile, "font-36", 36);
		largeFont = fontGen.createFont(fontFile, "font-50", 50);
		smallStyle = new Label.LabelStyle(smallFont, brown);
		medStyle = new Label.LabelStyle(medFont, brown);
		largeStyle = new Label.LabelStyle(largeFont, brown);
		
		font = new BitmapFont(Gdx.files.internal("fonts/poo.fnt"));
		//font.setScale(.25f, -.25f);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		easyFont = new BitmapFont(Gdx.files.internal("fonts/BRLNSR-12.fnt"));
		//easyFont.setScale(0.8f, -0.8f);
		easyFont.setColor(Color.NAVY);
		easyFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// Sounds from http://www.freesfx.co.uk/
		pooDrop = Gdx.audio.newSound(Gdx.files.internal("sounds/fart_2.wav"));
		pooLand = Gdx.audio.newSound(Gdx.files.internal("sounds/poopSplat.wav"));
		pooCaught = Gdx.audio.newSound(Gdx.files.internal("sounds/poopSound.wav"));
		targetHit = Gdx.audio.newSound(Gdx.files.internal("sounds/laugh_goofy.wav"));
		gameOver = Gdx.audio.newSound(Gdx.files.internal("sounds/laughing.wav"));
		
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/steel_road.mp3"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(0.7f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/steel_road.mp3"));
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		throwingMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/the_underworld_dark.mp3"));
		throwingMusic.setLooping(true);
	}

	public static void setHighScore(int val) {
		prefs.putInteger(PREFS_HIGH_SCORE, val);
		prefs.flush();
	}

	public static int getHighScore() {
		return prefs.getInteger(PREFS_HIGH_SCORE);
	}
	
	/**
	 * Get the list of targets paths
	 * @return Comma separated list of paths to various pictures to be used as targets
	 */
	public static String getTargetsList()
	{
		System.out.println("Getting target list: " + prefs.getString(PREFS_TARGET_LIST));
		return prefs.getString(PREFS_TARGET_LIST);
	}
	
	
	public static void addTargetToList(String target)
	{
		if (!target.isEmpty())
		{
			String list = AssetLoader.getTargetsList();
			list += target + ",";
			prefs.putString(PREFS_TARGET_LIST, list);
			prefs.flush();
			
			FileHandle entry = Gdx.files.external(target);
			if (entry.exists())
			{
				Texture targetTexture = new Texture(entry);
				targetTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
				TextureRegion newTarget = new TextureRegion(targetTexture);
				AssetLoader.targetsList.add(newTarget);
				System.out.println("Target Path: " + entry.path());
				AssetLoader.setTargetListIndex(targetsList.size() - 1);
				AssetLoader.setTargetImage(newTarget);
			}
		}
	}
	
	public static void setSoundSetting(Boolean val) {
		prefs.putBoolean(PREFS_SOUND, val);
		prefs.flush();
	}
	
	public static Boolean getSoundsSetting() {
		return prefs.getBoolean(PREFS_SOUND);
	}
	
	public static void setMusicSetting(Boolean val) {
		prefs.putBoolean(PREFS_MUSIC, val);
		prefs.flush();
		if (!val)
		{
			AssetLoader.gameMusic.stop();
			AssetLoader.menuMusic.stop();
		}
	}
	
	public static Boolean getMusicSetting() {
		return prefs.getBoolean(PREFS_MUSIC);
	}
	
	public static void setTargetPath(String val) {
		prefs.putString(PREFS_TARGET_PIC_PATH, val);
		prefs.flush();
	}
	
	private static Integer getTargetX() {
		System.out.println("Target's X: " + prefs.getInteger(PREFS_TARGET_PIC_X));
		return prefs.getInteger(PREFS_TARGET_PIC_X);
	}
	
	private static Integer getTargetY() {
		System.out.println("Target's Y: " + prefs.getInteger(PREFS_TARGET_PIC_Y));
		return prefs.getInteger(PREFS_TARGET_PIC_Y);
	}
	
	private static Integer getTargetWidth() {
		System.out.println("Target's Width: " + prefs.getInteger(PREFS_TARGET_PIC_WIDTH));
		return prefs.getInteger(PREFS_TARGET_PIC_WIDTH);
	}
	
	private static Integer getTargetHeight() {
		System.out.println("Target's Height: " + prefs.getInteger(PREFS_TARGET_PIC_HEIGHT));
		return prefs.getInteger(PREFS_TARGET_PIC_HEIGHT);
	}
	
	private static Integer getTargetListIndex() {
		System.out.println("Target's X: " + prefs.getInteger(PREFS_TARGET_INDEX));
		return prefs.getInteger(PREFS_TARGET_INDEX);
	}
	
	private static void setTargetListIndex(Integer newIndex) {
		AssetLoader.targetIndex = newIndex;
		prefs.putInteger(PREFS_TARGET_INDEX, newIndex);
		prefs.flush();
	}
	
	/**
	 * Updates the image used to throw poo at based on a new file that is selected by the user
	 * @param targetFilePath The new file path for the target image
	 * @param x the top left x coordinate of the entire image to start just the texture region
	 * @param y the top left y coordinate of the entire image to start just the texture region
	 * @param width How wide the selected region within the image is
	 * @param height How high the selected region within the image is
	 */
	public static void updateTargetImage(String targetFilePath, Integer x, Integer y, Integer width, Integer height, Boolean isInternal)
	{
		AssetLoader.prefs.putInteger(PREFS_TARGET_PIC_X, x);
		AssetLoader.prefs.putInteger(PREFS_TARGET_PIC_Y, y);
		AssetLoader.prefs.putInteger(PREFS_TARGET_PIC_WIDTH, width);
		AssetLoader.prefs.putInteger(PREFS_TARGET_PIC_HEIGHT, height);
		// Save the changes
		AssetLoader.prefs.flush();
		Texture targetTexture = null;
		if (isInternal)
		{
			targetTexture = new Texture(Gdx.files.internal(targetFilePath));
		}
		else
		{
			targetTexture = new Texture(Gdx.files.external(targetFilePath));
		}
		targetTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		AssetLoader.setTargetImage(new TextureRegion(targetTexture, 0 ,0 , 60, 60));
	}
	
	private static void setTargetImage(TextureRegion newTarget)
	{
		System.out.println("Setting target image");
		Texture targetTexture = newTarget.getTexture();
		// Update the target if it has already been created as references to it are held by the Renderer Class
		if (target == null)
		{
			target = new TextureRegion(targetTexture, AssetLoader.getTargetX(),AssetLoader.getTargetY(),AssetLoader.getTargetWidth(),AssetLoader.getTargetHeight());
		}
		else 
		{
			target.setRegion(new TextureRegion(targetTexture, AssetLoader.getTargetX(),AssetLoader.getTargetY(),AssetLoader.getTargetWidth(),AssetLoader.getTargetHeight()));
		}
		target.flip(false, true);
	}
	
	private static void refreshTargetImage()
	{
		AssetLoader.setTargetImage(AssetLoader.targetsList.get(AssetLoader.targetIndex));
	}
	
	/**
	 * Moves to the next target in the list, or back to the beginning if we're at the end
	 */
	public static void moveToNextTarget()
	{
		// Check we have targets
		if (AssetLoader.targetsList.size() > 1)
		{
			// If we're at the end then go to the start
			if (AssetLoader.targetIndex == (AssetLoader.targetsList.size()-1))
			{
				AssetLoader.targetIndex = 0;
			}
			else
			{
				AssetLoader.targetIndex++;
			}
			// Set the next image as the selected target
			System.out.println("Index updated to: " + AssetLoader.targetIndex);
			AssetLoader.setTargetListIndex(AssetLoader.targetIndex);
			AssetLoader.refreshTargetImage();
		}
	}
	
	/**
	 * Updates the target to the previous one in the list, or to the end of the list if we're at the beginning
	 */
	public static void moveToPreviousTarget()
	{
		// Check we have targets
		if (AssetLoader.targetsList.size() > 1)
		{
			// Check to see if we're at the start
			if (AssetLoader.targetIndex == 0)
			{
				AssetLoader.targetIndex = AssetLoader.targetsList.size()-1;
			}
			else
			{
				AssetLoader.targetIndex--;
			}
			// Set the next image as the selected target
			AssetLoader.setTargetListIndex(AssetLoader.targetIndex);
			AssetLoader.refreshTargetImage();
		}
	}
	
	/**
	 * Gets all the targets out of the internal folder and put them in an array.
	 * It then goes through the comma separated list of external files and puts their images in the array
	 */
	private static void getAllTargets()
	{
		TextureRegion newTarget;
		Texture targetTexture;
		FileHandle dirHandle = Gdx.files.internal(LOCAL_TARGETS_FOLDER_NAME);
		Integer index = 0;
		for (FileHandle entry: dirHandle.list()) {
			// Make each picture a texture and put it in the array
			if (entry.extension().equalsIgnoreCase("png")
					|| entry.extension().equalsIgnoreCase("jpg"))
			{
				targetTexture = new Texture(entry);
				targetTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
				newTarget = new TextureRegion(targetTexture);
				AssetLoader.targetsList.add(newTarget);
				System.out.println("Target Path: " + entry.path());
				if (index == AssetLoader.getTargetListIndex())
				{
					AssetLoader.targetIndex = index;
					System.out.println("Set Target to List Index: " + AssetLoader.targetIndex);
					AssetLoader.setTargetImage(newTarget);
				}
			}
			index++;
		}
		// Now get all from the external paths
		if (Gdx.files.isExternalStorageAvailable())
		{
			String list = AssetLoader.getTargetsList();
			for (String path : list.split(","))
			{
				System.out.println("Path: " + path);
				FileHandle entry = Gdx.files.external(path);
				if (!path.isEmpty() && entry.exists())
				{
					targetTexture = new Texture(entry);
					targetTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
					newTarget = new TextureRegion(targetTexture);
					AssetLoader.targetsList.add(newTarget);
					System.out.println("Target Path: " + entry.path());
					if (index == AssetLoader.getTargetListIndex())
					{
						AssetLoader.targetIndex = index;
						System.out.println("Set Target to List Index: " + AssetLoader.targetIndex);
						AssetLoader.setTargetImage(newTarget);
					}
					index++;
				}
			}
		}
	}
	
	/**
	 * Dispose all assets
	 */
	public static void dispose() {
		// We must dispose of the textures when we are finished.
		logoTexture.dispose();
		texture.dispose();
		font.dispose();
		targetHit.dispose();
		pooCaught.dispose();
		pooDrop.dispose();
		pooLand.dispose();
		gameMusic.dispose();
		gameOver.dispose();
		menuMusic.dispose();
		throwingMusic.dispose();
		for (TextureRegion region : targetsList)
		{
			region.getTexture().dispose();
		}
	}
	
}