package game.asteroids.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.asteroids.input.Input;
import game.asteroids.utility.Sprites;

/**
 * Player Entity, controllable by the player.
 * It has 3 abilities: shooting bullets, thrusting, and teleportation
 */
public class Player extends Entity {
	private final float turnSpeed = 20f;
	private final float speed = 15f;
	private final float maxSpeed = 20f;

	private final float shootSpeed = 200f;

	private Sprite shipBurn;

	public static int lives;
	public static int score;

	private Vector2 direction;

	public Player() {
		initialize(Sprites.PLAYER_SPRITE, Entity.LAYER_PLAYER);

		shipBurn = new Sprite(assets.get(Sprites.PLAYER_BURN, Texture.class));
		shipBurn.setOriginCenter();

		direction = new Vector2(0, 1);
		body.setLinearDamping(0.5f);
	}

	public Player(Vector2 position) {
		initialize(Sprites.PLAYER_SPRITE, Entity.LAYER_PLAYER);

		shipBurn = new Sprite(assets.get(Sprites.PLAYER_BURN, Texture.class));
		shipBurn.setOriginCenter();

		direction = new Vector2(0, 1);
		body.setTransform(position, body.getAngle());
		body.setLinearDamping(0.5f);
	}

	public void update() {
		// Thrusting
		direction.setAngleRad(body.getAngle() + MathUtils.degreesToRadians * 90);

		if (Input.keyDown(Input.UP)) {
			Vector2 thrust = new Vector2(direction).nor().scl(speed);
			body.applyForceToCenter(thrust, true);
		}

		body.setLinearVelocity(body.getLinearVelocity().clamp(0, maxSpeed));

		// Rotation
		body.setAngularVelocity(0);
		if (Input.keyDown(Input.LEFT)) body.applyTorque(turnSpeed, true);
		else if (Input.keyDown(Input.RIGHT)) body.applyTorque(-turnSpeed, true);

		//Shooting
		if (Input.keyPressed(Input.SPACE))
			new Bullet(Bullet.BulletType.PLAYER, direction.nor().scl(shootSpeed), body.getPosition());

		// Hyperjump
		if (Input.keyPressed(Input.LSHIFT))
			setPosition(randomPosition());

		wrap();
	}

	@Override
	public void draw(SpriteBatch batch, AssetManager manager) {
		Vector2 pos = body.getPosition();
		batch.draw(getSprite(), pos.x - sprite.getOriginX(), pos.y - sprite.getOriginY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), 1 / Sprites.PIXELS_PER_METER, 1 / Sprites.PIXELS_PER_METER, body.getAngle() * MathUtils.radiansToDegrees);
	}

	private Sprite getSprite(){
		if(Input.keyDown(Input.UP)){
			if(elapsedTicks % 3 == 0)
				return shipBurn;
			return sprite;
		}else{
			return sprite;
		}
	}
}
