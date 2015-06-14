package com.paterson.GameWorld;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.paterson.GameObjects.Cow;
import com.paterson.GameObjects.PooDropper;

/**
 * This class holds the config for each level such as the type of animal to use, the speed and frequency of the drops
 * and other such things. These are populated by GameLevelGenerator which pulls config from a file.
 * 
 * @author Craig Paterson
 *
 */
public class GameLevel {

	private static final String INITIAL_SETTINGS = "0,COW,60,1500,25,60";
	
	private static final int SETTINGS_LEVEL_INDEX = 0;
	private static final int SETTINGS_DROPPER_TYPE_INDEX = 1;
	private static final int SETTINGS_FALL_SPEED_INDEX = 2;
	private static final int SETTINGS_FALL_FREQUENCY_INDEX = 3;
	private static final int SETTINGS_CAPACITY_INDEX = 4;
	private static final int SETTINGS_CATCHER_SPEED_INDEX = 5;
	
	private int level;
	private String levelName;
	private PooDropper pooDropper;
	
	private int pooFallSpeed;
	private int timeBetweenFall;
	private int containerCapacity;
	private int catcherSpeed;
	private String dropperType;
	
	private GameWorld gameWorld;
	
	private BufferedReader levelInfoFile;
	
	private ArrayList<String> levelSettings;
	
	private GameLevel(GameWorld world)
	{
		this.level = 0;
		this.gameWorld = world;
		FileHandle levelInfo = Gdx.files.internal("data/levels.csv");
		levelInfoFile = new BufferedReader(levelInfo.reader());
		try {
			// Throw away the first line as it is the headings
			levelInfoFile.readLine();
			levelSettings = new ArrayList<String>();
			levelSettings.add(0, INITIAL_SETTINGS);
			// Second line is the first
			while (levelInfoFile.ready())
			{
				levelSettings.add(levelInfoFile.readLine());
			}
			
		}
		catch (IOException ex)
		{
			System.err.println("Error: " + ex.getMessage());
		}
		this.updateSettingsFromLine();
		
		// Will only instantiate the first level (will just be updated after this)
		this.pooDropper = getPooDropperInstance(dropperType);
		
	}
	
	private void updateSettingsFromLine()
	{
		String line = levelSettings.get(this.level);
		System.out.println("Line to read: " + line);
		String[] settingsArray = line.split(",");
		this.level = Integer.parseInt(settingsArray[SETTINGS_LEVEL_INDEX]);
		this.dropperType = settingsArray[SETTINGS_DROPPER_TYPE_INDEX];
		this.pooFallSpeed = Integer.parseInt(settingsArray[SETTINGS_FALL_SPEED_INDEX]);
		this.timeBetweenFall = Integer.parseInt(settingsArray[SETTINGS_FALL_FREQUENCY_INDEX]);
		this.containerCapacity = Integer.parseInt(settingsArray[SETTINGS_CAPACITY_INDEX]);
		this.catcherSpeed = Integer.parseInt(settingsArray[SETTINGS_CATCHER_SPEED_INDEX]);
	}
	
	public static GameLevel getFirstLevel(GameWorld world)
	{
		return new GameLevel(world);
	}

	private PooDropper getPooDropperInstance(String type)
	{
		System.out.println("Type: " + type);
		if (type.equalsIgnoreCase("COW"))
		{
			return new Cow(gameWorld, timeBetweenFall);
		}
		return null;
	}

	public int getFallSpeed() {
		return this.pooFallSpeed;
	}
	
	public int getTimeBetweenFall()
	{
		return this.timeBetweenFall;
	}
	
	public PooDropper getPooDropper()
	{
		return this.pooDropper;
	}
	
	public int getContainerCapacity()
	{
		return this.containerCapacity;
	}
	
	public String getLevelName()
	{
		return this.levelName;
	}
	
	public int getCatcherSpeed()
	{
		return this.catcherSpeed;
	}
	
	public void nextLevel()
	{
		this.level++;
		updateSettingsFromLine();
	}
	
	public void reset()
	{
		this.level = 0;
		updateSettingsFromLine();
	}

	public int getLevelNumber() {
		return this.level;
	}
	
}
