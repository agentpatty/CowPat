package com.paterson.UI;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paterson.Helpers.AssetLoader;

public class SimpleCheckBox extends ClickableObject {

	private Boolean isChecked = false;
	private Boolean isPressed = false;
	
	
	public SimpleCheckBox(float width, float height,
			Boolean isChecked) {
		super(width, height);
		this.isChecked = isChecked;
	}

	
	public void draw(SpriteBatch batcher, float x, float y) {
		super.draw(batcher, x, y);
		// Draw the box first then the "check" after (so it is on top) if required
		batcher.draw(AssetLoader.checkBox, x, y, this.width, this.height);
		if (isChecked) {
			batcher.draw(AssetLoader.pooSplat, x -1, y -1, this.width+2, this.height+2);
		}
	}

	public boolean isTouchDown(int screenX, int screenY) {
		if (isClicked(screenX, screenY)) {
			isPressed = true;
			return true;
		}

		return false;
	}

	public boolean isTouchUp(int screenX, int screenY) {
		// It only counts as a touchUp if the button is in a pressed state.
		if (isClicked(screenX, screenY) && isPressed) {
			isPressed = false;
			// Switch whether it is checked or not
			this.isChecked = !this.isChecked;
			return true;
		}
		
		// Whenever a finger is released, we will cancel any presses.
		isPressed = false;
		return false;
	}

	public Boolean isChecked() {
		return this.isChecked;
	}

}
