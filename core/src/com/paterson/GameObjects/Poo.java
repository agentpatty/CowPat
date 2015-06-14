package com.paterson.GameObjects;

public class Poo extends Faller {
	
	private boolean hasHitGround;

	public Poo(PooDropper pooDropper, float width, float height, int groundLocation, int fallSpeed) {
		super(pooDropper, width, height, groundLocation, fallSpeed);
	}

	
	public void update(float delta) {
		super.update(delta);
	}


	@Override
	public boolean hitGround()
	{
		boolean hitGround = super.hitGround();
		if (hitGround && !hasHitGround)
		{
			// Adjust location for the flattened poo image (just once though)
			this.hasHitGround = true;
			this.width = this.width + 2;
			this.height = this.height - 1;
			this.position.y += 1; // adjust for change in height
		}
		return hitGround;
	}
}
