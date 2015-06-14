package com.paterson.GameObjects;

import com.badlogic.gdx.math.Vector2;

public abstract class MovingDrawableObject extends DrawableObject {
	protected Vector2 velocity;

	public MovingDrawableObject()
	{
		super();
		this.velocity = new Vector2(0,0);
	}
	
	public MovingDrawableObject(float width, float height)
	{
		super(width,height);
		this.velocity = new Vector2(0,0);
	}
	
	public MovingDrawableObject(float x, float y, float width, float height) {
		super(x,y,width,height);
		this.velocity = new Vector2(0,0);
	}
	
	public MovingDrawableObject(float x, float y, float width, float height, float velocityX, float velocityY) {
		super(x,y,width,height);
		this.velocity = new Vector2(velocityX, velocityY);
	}

	public void setVelocity(float x, float y)
	{
		this.velocity.set(x, y);
	}
	
}
