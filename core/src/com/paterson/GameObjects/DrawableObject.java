package com.paterson.GameObjects;

import com.badlogic.gdx.math.Vector2;

public abstract class DrawableObject {

	protected Vector2 position;
	
	protected float width;
	protected float height;
	
	public DrawableObject()
	{
		position = new Vector2(0,0);
	}
	
	public DrawableObject(float width, float height)
	{
		position = new Vector2(0,0);
		this.width = width;
		this.height = height;
	}
	
	public DrawableObject(float x, float y ,float width, float height)
	{
		position = new Vector2(x,y);
		this.width = width;
		this.height = height;
	}
	
	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	
	public abstract void update(float delta);
}
