package com.paterson.GameObjects;

import com.paterson.GameWorld.GameWorld;

public class Cow extends PooDropper {
	private static final int TIME_TO_DROP = 20;
	private static final int COW_WIDTH = 17;
	private static final int COW_HEIGHT = 12;
	
	
	public Cow(GameWorld world, int timeBetweenFall) {
		super(world, COW_WIDTH, COW_HEIGHT, timeBetweenFall);
		setVelocity(0, 120);
	}

	@Override
	public float getPooperX()
	{
		return getX() + 4;
	}
	
	@Override
	public float getPooperY()
	{
		return getY() + 5;
	}
	
	@Override
	public int getTimeToDrop() {
		return TIME_TO_DROP;
	}
	
	
}
