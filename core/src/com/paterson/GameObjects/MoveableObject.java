package com.paterson.GameObjects;


public class MoveableObject extends DrawableObject {
	
	public MoveableObject()
	{
		super();
	}
	
	public MoveableObject(float width, float height) {
		super(width,height);
	}
	
	public MoveableObject(float x, float y ,float width, float height)
	{
		super(x,y,width,height);
	}

	public void update(float delta) {
		// Don't do any automatic update - updates are triggered by the input handler
	}
	
	public void setXPosition(float x)
	{
		this.position.x = x;
	}
}
