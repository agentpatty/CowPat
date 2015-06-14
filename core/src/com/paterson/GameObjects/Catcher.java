package com.paterson.GameObjects;

import com.badlogic.gdx.math.Vector2;

public abstract class Catcher extends MovingDrawableObject {

	public static final int MOVER_SPEED = 60;
	
	protected Vector2 acceleration;	
	
	private boolean isMovingRight = true;
	
	public Catcher(int x, int y, int width, int height, int speed)
	{
		super(x,y,width,height,speed,0);
	}

	public float getMidPoint()
	{
		float middle = position.x + (width / 2);
		return middle;
	}
	

	public void update(float delta) {
		// Move them right or left
		if (isMovingRight)
		{
			position.add(velocity.cpy().scl(delta));
		}
		else
		{
			position.sub(velocity.cpy().scl(delta));
		}
	}
	
	public void reset()
	{
		
	}
	
	public void setSpeed(Integer newSpeed)
	{
		this.velocity.set(newSpeed, 0);
	}
	
	
	public void setMovingLeft()
	{
		this.isMovingRight = false;
	}
	
	public void setMovingRight()
	{
		this.isMovingRight = true;
	}
	
	public boolean isMovingRight()
	{
		return this.isMovingRight;
	}
	
	public float getXOffsetForContainer()
	{
		if (isMovingRight())
		{
			return 11;
		}
		else 
		{
			return 3;
		}
	}
}
