package com.paterson.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class SimpleImageButton extends ClickableObject {

	private TextureRegion buttonUp;
	private TextureRegion buttonDown;

	
	private boolean isPressed = false;

	public SimpleImageButton(float width, float height,
			TextureRegion buttonUp, TextureRegion buttonDown) {
		super(width, height);
		this.buttonUp = buttonUp;
		this.buttonDown = buttonDown;

	}

	public void draw(SpriteBatch batcher, float x, float y) {
		super.draw(batcher, x, y);
		if (isPressed) {
			batcher.draw(buttonDown, x, y, width, height);
		} else {
			batcher.draw(buttonUp, x, y, width, height);
		}
	}

	public boolean isTouchDown(int screenX, int screenY) {

		if (this.isClicked(screenX, screenY)) {
			isPressed = true;
			return true;
		}

		return false;
	}

	public boolean isTouchUp(int screenX, int screenY) {
		
		// It only counts as a touchUp if the button is in a pressed state.
		if (this.isClicked(screenX, screenY) && isPressed) {
			isPressed = false;
			return true;
		}
		
		// Whenever a finger is released, we will cancel any presses.
		isPressed = false;
		return false;
	}

	@Override
	public void addToStage(Stage stage, int screenX, int screenY) {
		// Nothing for now
	}
}
