package com.paterson.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class ClickableObject {
	/**
	 * Protected variables so that sub-classes can use them - particularly to help render the objects
	 */
	protected float width, height;

	private Rectangle bounds;
	
	public ClickableObject(float width, float height)
	{
		this.width = width;
		this.height = height;
		
		// Create the bounds 1 pixel larger than the size 
		bounds = new Rectangle(0, 0, width+2, height+2);

	}
	
	public float getWidth()
	{
		return this.width;
	}
	
	/**
	 * Defines if the area passed in is within the area of this click-able object
	 * @param screenX The x co-ordinate of the click
	 * @param screenY The y co-ordinate of the click
	 * @return True if the passed in co-ordinates intersect with the bounded area of this object (as passed to the constructor)
	 */
	public boolean isClicked(int screenX, int screenY) {
		return bounds.contains(screenX, screenY);
	}

	/**
	 * Draw this click-able object
	 * @param batcher The batcher that can render images and font
	 */
	public void draw(SpriteBatch batcher, float x, float y) {
		bounds.setX(x-1);
		bounds.setY(y-1);
	}
	/**
	 * Does the start of the click within the bounds of this object?
	 * @param screenX The x co-ordinate of the click
	 * @param screenY The y co-ordinate of the click
	 * @return True if the x and y co-ordinates of the click intersect with the bounds of this object
	 */
	public abstract boolean isTouchDown(int screenX, int screenY);
	/**
	 * Does the end of the click finish within the bounds of this object?
	 * @param screenX The x co-ordinate of the release
	 * @param screenY The y co-ordinate of the release
	 * @return True if the x and y co-ordinates of the click intersect with the bounds of this object
	 */
	public abstract boolean isTouchUp(int screenX, int screenY);
}
