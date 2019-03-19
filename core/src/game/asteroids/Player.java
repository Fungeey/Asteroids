package game.asteroids;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import game.asteroids.Input.Input;

public class Player extends Entity{
	private final float turnSpeed = 20f;
	private final float speed = 10f;
	private final float maxSpeed = 10f;

	public static int lives;
	public static int score;

	private Vector2 direction;

	public Player(World world, BodyEditorLoader loader){
		super(world);

		FixtureDef fix = new FixtureDef();
		fix.density = 10;
		fix.friction = 0.5f;
		fix.restitution = 0.3f;

		loader.attachFixture(body, "ship.png", fix, 1);

		direction = new Vector2(0, 1);
		position = new Vector2(0, 0);

		body.setLinearDamping(0.5f);
	}

	public void update(){
		direction.setAngleRad(body.getAngle() + MathUtils.degreesToRadians * 90);

		if(Input.keyDown(Input.UP)) {
			System.out.println(body.getAngle() * MathUtils.radiansToDegrees);
			Vector2 thrust = new Vector2(direction).nor();
			body.applyForceToCenter(thrust.scl(speed), true);
		}

		body.setLinearVelocity(body.getLinearVelocity().clamp(0, maxSpeed));

		body.setAngularVelocity(0);
		if(Input.keyDown(Input.LEFT)) body.applyTorque(turnSpeed, true);
		else if(Input.keyDown(Input.RIGHT)) body.applyTorque(-turnSpeed, true);


	}
}
