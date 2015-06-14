package com.paterson.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Faller extends MovingDrawableObject {
	
	public static final int MAX_POO_WIDTH = 6;
	public static final int MIN_POO_WIDTH = 4;
	public static final int POO_HEIGHT = 4;
	
	protected PooDropper pooDropper;
	
	protected int ground;
	
	protected Rectangle collisionArea;
	
	private boolean hasScored = false;
	private boolean recordedFallen = false;
	
	
	public Faller(PooDropper pooDropper, float width, float height, int groundLocation, int fallSpeed) {
		super(pooDropper.getPooperX(), pooDropper.getPooperY(),width, height, 0, fallSpeed);
		this.pooDropper = pooDropper;
		this.ground = groundLocation;
		collisionArea = new Rectangle(getX(), getY(), width, height);
	}
	
	public void update(float delta) {
		// If the poo has hit the ground, then go back to the top
		if (!hitGround())
		{
			position.add(velocity.cpy().scl(delta));
			updateCollisionArea();
		}
	}
	
	public void updateReady(float runTime) {
	}


	public void onRestart() {
		position.y = pooDropper.getPooperY();
		position.x = pooDropper.getPooperX();
		this.hasScored = false;
		this.recordedFallen = false;
		updateCollisionArea();
	}
	
	public void reset(int size, int fallSpeed)
	{
		this.onRestart();
		this.setFallSpeed(fallSpeed);
		width = size;
		height = size - 1;
	}

	
	public boolean hitGround()
	{
		return position.y + height >= this.ground + 4;
	}
	
	public Rectangle getCollisionArea()
	{
		return this.collisionArea;
	}
	
	private void updateCollisionArea()
	{
		this.collisionArea.setPosition(getX(), getY());
	}

	/**
	 * Mark this faller has having scored (i.e. been caught in the bucket).
	 */
	public void setScored()
	{
		this.hasScored = true;
	}
	
	public boolean hasScored()
	{
		return hasScored;
	}

	/**
	 * Mark this faller as haven fallen (i.e. hit the ground).
	 */
	public void recordFallen()
	{
		this.recordedFallen = true;
	}
	
	public boolean hasRecordedFallen()
	{
		return this.recordedFallen;
	}
	
	public void setFallSpeed(int fallSpeed)
	{
		if (this.velocity == null)
		{
			this.velocity = new Vector2(0, fallSpeed);
		}
		else
		{
			this.velocity.y = fallSpeed;
		}
	}
	
	/**
	 * Returns how many points this faller is worth. This does not necessarily mean this number of points will be added to the score (as a mutliplier can be used).
	 * @return The base number of points this object is worth. 
	 */
	public int getScoringValue()
	{
		return (int)this.getWidth();
	}
}
