package game.asteroids.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import game.asteroids.utility.Sprites;

public class Bullet extends Entity{
	private static final int bulletRadius = 6;
	private BulletType type;

	Bullet(BulletType type, Vector2 velocity){
		super(true);

		this.type = type;

		initialize(getSprite(), getShape(), LAYER_PLAYER_BULLET);
		body.setAngularDamping(Float.MAX_VALUE);
		body.applyForceToCenter(velocity, true);
	}

	Bullet(BulletType type, Vector2 velocity, Vector2 position){
		super(true);
		this.type = type;

		initialize(getSprite(), getShape(), LAYER_PLAYER_BULLET);
		setPosition(position);
		body.setAngularDamping(Float.MAX_VALUE);
		body.applyForceToCenter(velocity, true);
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

	public enum BulletType {
		PLAYER, SAUCER
	}

	public void update(){
		wrap();
	}
}
