package com.paterson.GameObjects;

import com.badlogic.gdx.math.MathUtils;
import com.paterson.GameWorld.GameWorld;

public abstract class PooDropper extends MovingDrawableObject {

	/** How far down the screen the image should go before bouncing back up */
	private static final int HEIGHT_TO_GO_DOWN_TO = 5;
	private static final int MAX_DIFFERENCE = 70;
	
	private int timeBetweenFall;
	private float timeSinceLastFall;
	private boolean goingDown;
	
	protected GameWorld gameWorld;
	
	public PooDropper(GameWorld world, int width, int height, int timeBetweenFall) {
		super(MathUtils.random(0, world.getGameWidth() - 10), -height, width, height);
		this.goingDown = true;
		this.gameWorld = world;
		this.timeBetweenFall = timeBetweenFall;
		// Velocity will change per sub-class, but initialise here.
		if (position.x > MAX_DIFFERENCE)
		{
			position.x = MAX_DIFFERENCE;
		}
	}
	
	public void update(float delta) {
		timeSinceLastFall += (delta*1000);

		if (position.y < HEIGHT_TO_GO_DOWN_TO && this.goingDown)
		{
			// Need to keep going down
			position.add(velocity.cpy().scl(delta));
		}
		else if (position.y >= HEIGHT_TO_GO_DOWN_TO && this.goingDown)
		{
			// Got all the way down, drop a poo start going back up
			this.goingDown = false;
			this.gameWorld.dropPoo();
			timeSinceLastFall = 0;
		}
		else if (position.y > -height && !this.goingDown)
		{
			// keep going up
			position.sub(velocity.cpy().scl(delta));
		}
		else if (position.y <= -height && (timeSinceLastFall + getTimeToDrop() >= timeBetweenFall))
		{
			// Image is off the top of the screen at this point
			// Move to random x location and start going down again once it is time to drop another poo
			this.updateXPosition();
			this.goingDown = true;
		}
	}
	
	private void updateXPosition()
	{
		float difference = MathUtils.random(5, gameWorld.getGameWidth() - (5 + this.width)) - position.x;
		// Ensure that the difference in X isn't too far to be possible to get so reduce it
		if (difference > MAX_DIFFERENCE)
		{
			difference = MAX_DIFFERENCE;
		}
		else if (difference < -MAX_DIFFERENCE)
		{
			difference = -MAX_DIFFERENCE;
		}
		
		position.x += difference;
	}
	
	public void reset()
	{
		this.timeSinceLastFall = 0;
		this.goingDown = true;
		this.updateXPosition();
		position.y = -height;
	}
	
	public int getFallFrequency() {
		return timeBetweenFall;
	}

	public void setFallFrequency(int fallFrequency) {
		this.timeBetweenFall = fallFrequency;
	}

	public void onRestart(int y) {
		position.y = y;
	}

	// Abstract methods that each subclass needs to implement
	public abstract float getPooperX();
	public abstract float getPooperY();
	public abstract int getTimeToDrop();
}
