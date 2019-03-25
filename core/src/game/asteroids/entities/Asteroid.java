package game.asteroids.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.asteroids.screens.TestScreen;
import game.asteroids.utility.Sprites;

/**
 * Asteroid Entity, comes in 3 sizes, each with their own sprite and point value
 */
public class Asteroid extends Entity implements Destructable {
	private static final float startVelocity = 50f;

	private AsteroidSize size;

	public Asteroid(AsteroidSize size) {
		this.size = size;

		initialize(getSprite(), LAYER_ASTEROIDS);
		applyRandomVelocity();
	}

	public Asteroid(AsteroidSize size, Vector2 position) {
		this.size = size;

		initialize(getSprite(), LAYER_ASTEROIDS);
		body.setTransform(position, body.getAngle());
		applyRandomVelocity();
	}

	private String getSprite() {
		if (size == AsteroidSize.SMALL)
			return Sprites.ASTEROID_SMALL;
		if (size == AsteroidSize.MEDIUM)
			return Sprites.ASTEROID_MEDIUM;
		return Sprites.ASTEROID_LARGE;
	}

	private void randomizeAngle() {
		body.setTransform(body.getPosition(), TestScreen.rand.nextInt(360) * MathUtils.degreesToRadians);
	}

	private void applyRandomVelocity() {
		randomizeAngle();
		Vector2 thrust = new Vector2(0, 1).setAngleRad(body.getAngle() + MathUtils.degreesToRadians * 90);
		body.applyForceToCenter(thrust.nor().scl(TestScreen.rand.nextFloat() * startVelocity), true);
		body.applyTorque(10f, true);
	}

	@Override
	public int getPointValue() {
		switch (size) {
			case SMALL:
				return 100;
			case MEDIUM:
				return 50;
			case LARGE:
				return 20;
		}
		return 0;
	}

	public enum AsteroidSize {
		SMALL, MEDIUM, LARGE
	}

	public void update() {
		wrap();
	}

	@Override
	public void onDestroy() {
		if(size == AsteroidSize.LARGE){
			new Asteroid(AsteroidSize.MEDIUM, body.getPosition());
			new Asteroid(AsteroidSize.MEDIUM, body.getPosition());
		}else if(size == AsteroidSize.MEDIUM){
			new Asteroid(AsteroidSize.SMALL, body.getPosition());
			new Asteroid(AsteroidSize.SMALL, body.getPosition());
		}
	}
}
