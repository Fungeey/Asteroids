package game.asteroids.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Button extends DrawableElement {
	private Vector2 size;
	private Runnable action;
	private String label;
	private Color color;
	private Color hoverColor;

	public Button(Vector2 size, Runnable action, String label) {
		this.size = size;
		this.action = action;
		this.label = label;
		this.color = Color.FIREBRICK;
		this.hoverColor = Color.RED;
	}

	@Override
	public void draw() {
		if (hovering() && Gdx.input.justTouched()) {
			action.run();
		}
	}

	private boolean hovering() {
		int mouseX = Gdx.input.getX();
		int mouseY = Gdx.input.getY();
		return mouseX > position.x && mouseX < position.x + size.x &&
				mouseY > position.y && mouseY < position.y + size.y;
	}
}
