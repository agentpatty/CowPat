package com.paterson.GameObjects;

/**
 * A class that will be used to hold the location of the projectile (poo) that is moving towards the target
 * @author Craig Paterson
 *
 */
public class Projectile extends MovingDrawableObject {

	private static final int HIT_MARGIN_OF_ERROR = 10;
	private static final int HIT_SLIGHTLY_MARGIN_OF_ERROR = 40;
	
	private Boolean isMoving = false;
	
	public Projectile() {
		super();
	}

	public Projectile(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public Projectile(float width, float height) {
		super(width, height);
	}

	@Override
	public void update(float delta) {
		position.sub(velocity.cpy().scl(delta));
	}

	public void startMoving(float startingX, float startingY)
	{
		this.position.set(startingX, startingY);
		this.isMoving = true;
	}
	
	public void stopMoving()
	{
		this.isMoving = false;
	}
	
	public Boolean getIsMoving()
	{
		return this.isMoving;
	}
	
	public void reset()
	{
		this.setVelocity(0, 0);
		this.position.set(0, 0); // put it off the screen for sure
	}

	/**
	 * Checks to see if the projectile managed to hit the target or not
	 * @param target The target to check the projectile hit
	 * @return true if the projectile hit the target within the set margin of error
	 */
	public Boolean hitTarget(Target target) {
		float targetCentreX = target.getX() + (target.getWidth()/2);
		float projectileCentreX = this.getX() + (this.getWidth()/2);
		return !this.isMoving && Math.abs(targetCentreX-projectileCentreX) <= HIT_MARGIN_OF_ERROR;
	}

	/**
	 * Check to see if the projectile partially hit the target. If this is true, then hitTarget() will also be true.
	 * @param target The target to check the projectile hit
	 * @return true if the projectile hit the target within the enlarged slightly hit margin of error
	 */
	public Boolean hitTargetSlightly(Target target) {
		float targetCentreX = target.getX() + (target.getWidth()/2);
		float projectileCentreX = this.getX() + (this.getWidth()/2);
		return !this.isMoving && Math.abs(targetCentreX-projectileCentreX) <= HIT_SLIGHTLY_MARGIN_OF_ERROR;
	}
	
}
