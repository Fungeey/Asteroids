package game.asteroids.utility;

import com.badlogic.gdx.math.Vector2;

/**
 * Abstract class that represents an object that can be drawn on the screen.
 */
public abstract class DrawableElement {
	protected Vector2 position;

	/**
	 * Runs every frame to process logic and draw the element on the screen.
	 */
	public abstract void draw();

}
