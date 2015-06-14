package com.paterson.GameObjects;

public class StandardCatcher extends Catcher {

	public StandardCatcher(int x, int y, int width, int height, int speed) {
		super(x, y, width, height, speed);
	}

	@Override
	public void setMovingLeft() {
		super.setMovingLeft();
		// Adjust for tail so that head is in the same place
		this.position.x += 6;
	}

	@Override
	public void setMovingRight() {
		super.setMovingRight();
		// Adjust for tail so that head is in the same place
		this.position.x -= 6;
	}

	@Override
	public float getMidPoint() {
		float middle = super.getMidPoint();
		if  (isMovingRight())
		{
			middle+= 5;
		}
		else 
		{
			middle -= 5;
		}
		return middle;
	}

	
	
}
