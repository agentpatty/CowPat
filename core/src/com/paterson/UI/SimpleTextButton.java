package com.paterson.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paterson.Helpers.AssetLoader;

public class SimpleTextButton extends ClickableObject {


	private String buttonText;


	private boolean isPressed = false;

	public SimpleTextButton(float width, float height,
			String buttonText) {
		super(width, height);
		this.buttonText = buttonText;

	}

	public void draw(SpriteBatch batcher, float x, float y) {
		super.draw(batcher, x, y);
		if (isPressed) {
			AssetLoader.shadowFont.draw(batcher, buttonText, x + 1, y + 1);
		}
		AssetLoader.font.draw(batcher, buttonText, x, y);
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
			return true;
		}
		
		// Whenever a finger is released, we will cancel any presses.
		isPressed = false;
		return false;
	}

}
