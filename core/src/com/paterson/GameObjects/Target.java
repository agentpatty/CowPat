package com.paterson.GameObjects;

import com.badlogic.gdx.math.Vector2;

/**
 * A class to hold the position of the target for poo to be thrown at
 * @author Craig Paterson
 *
 */
public class Target extends MovingDrawableObject {

	public static final int TARGET_SPEED = 40;
	
	protected Boolean isMovingRight = true;
	
	public Target(int x, int y, int width, int height)
	{
		super(x,y,width,height);
		velocity = new Vector2(TARGET_SPEED, 0);
	}
	
	@Override
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
	
	public void setMovingLeft()
	{
		this.isMovingRight = false;
	}
	
	public void setMovingRight()
	{
		this.isMovingRight = true;
	}

	public Boolean isMovingRight()
	{
		return this.isMovingRight;
	}
}
