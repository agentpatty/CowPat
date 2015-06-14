package com.paterson.GameObjects;

import com.badlogic.gdx.math.Rectangle;

public abstract class Container extends DrawableObject {

	
	/**	The amount of poo this container can hold */
	private int capacity;
	/**	The amount of poo currently in the container */
	private int currentLoad;
	
	private Rectangle containerOpening;

	private boolean isMovingRight = true;
	
	private Catcher catcher;
	
	public Container(int width, int height, int capacity, Catcher catcher)
	{
		super(0,0,width,height);
		this.capacity = capacity;
		this.currentLoad = 0;
		this.containerOpening = new Rectangle(0,0,width - 4,0);
		this.catcher = catcher;
		this.update(0);
	}

	/**
	 * This method updates the current state of the Container, ensuring it is still on top of the catcher 
	 * @param delta Time between the last update
	 * @param catcherX The X position of the catcher plus an offset that will be the position of this container
	 * @param catcherY The Y position of the catcher. This will be offset by this containers height
	 */
	public void update(float delta) {
		this.position.x = catcher.getX() + catcher.getXOffsetForContainer();
		this.position.y = catcher.getY() - height;
		this.containerOpening.setPosition(getX() + 2, getY() + 2);
	}
	
	public void reset()
	{
		currentLoad = 0;
	}
	
	public void setMovingLeft()
	{
		this.isMovingRight = false;
	}
	
	public void setMovingRight()
	{
		this.isMovingRight = true;
	}
	
	public boolean isMovingright()
	{
		return this.isMovingRight;
	}
	
	/**
	 * Gets the opening collision area for the container to determine if a faller has been caught
	 * @return The rectangle collision area of the opening of the container
	 */
	public Rectangle getContainerOpening()
	{
		return this.containerOpening;
	}
	
	/**
	 * Setter for the maximum this container can hold. Assumed that container is empty at time of calling.
	 * @param newCapacity The new capacity of the container
	 */
	public void setCapacity(int newCapacity)
	{
		this.capacity = newCapacity;
	}
	
	/**
	 * Method to add load into the container to fill it up
	 * @param load The amount to load into the container
	 */
	public void fillContainer(int load)
	{
		this.currentLoad += load;
	}
	
	/**
	 * returns true if the container has reached it's capacity
	 * @return true = container full, false = container not yet full
	 */
	public boolean isContainerFull()
	{
		return this.currentLoad >= this.capacity;
	}
	
	/**
	 * Find how full the container is
	 * @return the percentage full the container is in decimals (max = 1)
	 */
	public float getPercentFull()
	{
		float percentFull = (float)this.currentLoad / this.capacity;
		// Can't be more than 100% full
		if (percentFull > 1)
		{
			percentFull = 1;
			
		}
		return percentFull;
	}
}
