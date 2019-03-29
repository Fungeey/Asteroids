package game.asteroids.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.asteroids.PhysicsEngine;
import game.asteroids.screens.GameScreen;
import game.asteroids.utility.Sprites;
import systems.CollisionHandler;

import java.util.Random;

/**
 * Asteroid Entity, comes in 3 sizes, each with their own sprite and point value
 */
public class Asteroid extends Entity implements Destructable {
	private static final float startVelocity = 50f;

	private AsteroidSize size;

	public Asteroid(PhysicsEngine engine, AsteroidSize size) {
		this(engine, size, Vector2.Zero);
	}

	public Asteroid(PhysicsEngine engine, AsteroidSize size, Vector2 position) {
		this(engine, size, position, Vector2.Zero);
		applyRandomVelocity();
	}

	public Asteroid(PhysicsEngine engine, AsteroidSize size, Vector2 position, Vector2 velocity) {
		super(engine);
		this.size = size;

		initialize(getSprite(), CollisionHandler.LAYER_ASTEROIDS);
		body.setTransform(position, body.getAngle());
		body.setLinearVelocity(velocity);
		Random random = new Random();
		body.applyTorque((random.nextFloat() + 1) * body.getMass() * (random.nextBoolean() ? 1 : -1), true);
		engine.numAsteroids++;
	}

	private String getSprite() {
		if (size == AsteroidSize.SMALL)
			return Sprites.ASTEROID_SMALL;
		if (size == AsteroidSize.MEDIUM)
			return Sprites.ASTEROID_MEDIUM;
		return Sprites.ASTEROID_LARGE;
	}

	private void applyRandomVelocity() {
		body.setTransform(body.getPosition(), GameScreen.rand.nextInt(360) * MathUtils.degreesToRadians);
		Vector2 thrust = new Vector2(0, 1).setAngleRad(body.getAngle() + MathUtils.degreesToRadians * 90);
		body.applyForceToCenter(thrust.nor().scl(GameScreen.rand.nextFloat() * startVelocity * body.getMass()), true);
		body.applyTorque(10f, true);
	}

	@Override
	public int getPointValue() {
		if(engine.isOnDeleteList(this))
			return 0;

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

	@Override
	public void update() {
		wrap();
	}

	@Override
	public void onDestroy() {
		if(size == AsteroidSize.LARGE){
			for (int i = 0; i < 2; i++) {
				Asteroid a = new Asteroid(engine, AsteroidSize.MEDIUM, body.getPosition());
				a.body.setLinearVelocity(body.getLinearVelocity().scl(0.25f));
			}
		}else if(size == AsteroidSize.MEDIUM){
			for (int i = 0; i < 2; i++) {
				Asteroid a = new Asteroid(engine, AsteroidSize.SMALL, body.getPosition());
				a.body.setLinearVelocity(body.getLinearVelocity().scl(0.25f));
			}
		}
		engine.numAsteroids--;
	}
}
