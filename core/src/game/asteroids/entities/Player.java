package game.asteroids.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import game.asteroids.BodyEditorLoader;
import game.asteroids.input.Input;
import game.asteroids.utility.Sprites;

public class Player extends Entity{
	private final float turnSpeed = 20f;
	private final float speed = 15f;
	private final float maxSpeed = 20f;

	public static int lives;
	public static int score;

	private Vector2 direction;

	public Player(){
		this(Vector2.Zero);
	}

	public Player(Vector2 position){
		super(Sprites.PLAYER_SPRITE);

		direction = new Vector2(0, 1);
		body.setTransform(position, body.getAngle());

		body.setLinearDamping(0.5f);
	}

	public void update(){
		// Thrusting
		direction.setAngleRad(body.getAngle() + MathUtils.degreesToRadians * 90);

		if(Input.keyDown(Input.UP)) {
			Vector2 thrust = new Vector2(direction).nor().scl(speed);
			body.applyForceToCenter(thrust, true);
		}

		body.setLinearVelocity(body.getLinearVelocity().clamp(0, maxSpeed));

		// Rotation
		body.setAngularVelocity(0);
		if(Input.keyDown(Input.LEFT)) body.applyTorque(turnSpeed, true);
		else if(Input.keyDown(Input.RIGHT)) body.applyTorque(-turnSpeed, true);

		//Shooting
		//if(Input.keyDown(Input.SPACE)){
		//	new Bullet(Bullet.BulletType.PLAYER, direction);
		//}

		// Hyperjump
		if(Input.keyDown(Input.LSHIFT))
			setPosition(randomPosition());

		wrap();
	}
}
