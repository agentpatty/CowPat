package com.paterson.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class SimpleTextButton extends ClickableObject {

	private Label textLabel, shadowLabel;
	
	private boolean isPressed = false;

	public SimpleTextButton(float width, float height,
			String buttonText, LabelStyle fontStyle) {
		super(buttonText.length()* 11f, height);
		this.textLabel = new Label(buttonText, fontStyle);
		this.shadowLabel = new Label(buttonText, fontStyle);
	}

	public void draw(SpriteBatch batcher, float x, float y) {
		super.draw(batcher, x, y);
		textLabel.setPosition(x, y);
		shadowLabel.setPosition(x+1, y+1);
		if (isPressed) {
			shadowLabel.setColor(64, 64, 64, 10); // Set to grey for shadow on click
		}
		else
		{
			shadowLabel.setColor(0, 0, 0, 10); // Set to black for shadow
		}
		shadowLabel.draw(batcher, 1);
		textLabel.draw(batcher, 1);
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

	@Override
	public void addToStage(Stage stage, int screenX, int screenY) {
		super.addToStage(stage, screenX, screenY);
		textLabel.setPosition(screenX, screenY);
		shadowLabel.setPosition(screenX+1, screenY+1);
		if (isPressed) {
			shadowLabel.setColor(64, 64, 64, 10); // Set to grey for shadow on click
		}
		else
		{
			shadowLabel.setColor(0, 0, 0, 10); // Set to black for shadow
		}
		stage.addActor(textLabel);
		stage.addActor(shadowLabel);
	}
}
