package game.asteroids.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import game.asteroids.PhysicsEngine;
import game.asteroids.utility.Sprites;
import systems.CollisionHandler;
import systems.Timer;

/**
 * Bullet Entity, comes in two types, Player and Saucer, based on how they how different properties
 */
public class Bullet extends Entity{
	private static final int bulletRadius = 6;
	private static final float lifeTime = 0.75f;

	private Timer lifeTimer;

	private BulletType type;

	Bullet(PhysicsEngine engine, BulletType type, Vector2 velocity, Vector2 position) {
		super(engine, true);
		this.type = type;
		setPosition(position);

		if(type == BulletType.PLAYER)
			initialize(getSprite(), getShape(), CollisionHandler.LAYER_PLAYER_BULLET);
		else
			initialize(getSprite(), getShape(), CollisionHandler.LAYER_SAUCER_BULLET);
		body.setLinearVelocity(velocity);

		lifeTimer = Timer.startNew(lifeTime, this::delete);
	}

	private Shape getShape(){
		CircleShape circle = new CircleShape();
		circle.setRadius(bulletRadius/Sprites.PIXELS_PER_METER);

		return circle;
	}

	private String getSprite(){
		if(type == BulletType.PLAYER)
			return Sprites.BULLET_PLAYER;
		return Sprites.BULLET_SAUCER;
	}

	@Override
	public void delete(){
		super.delete();
		lifeTimer.clear();
	}


	public enum BulletType {
		PLAYER, SAUCER
	}

	public void update(){
		if(wrap() && type == BulletType.SAUCER)
			this.delete();
	}

}
