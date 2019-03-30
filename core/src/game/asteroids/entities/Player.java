package game.asteroids.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.asteroids.PhysicsEngine;
import game.asteroids.input.Input;
import game.asteroids.screens.GameScreen;
import game.asteroids.utility.Sprites;
import systems.CollisionHandler;
import systems.Sounds;
import systems.Timer;

import java.util.ArrayList;

/**
 * Player Entity, controllable by the player.
 * It has 3 abilities: shooting bullets, thrusting, and teleportation
 */
public class Player extends Entity {
	private static final float turnSpeed = 10f;
	private static final float speed = 20f;
	private static final float maxSpeed = 15f;
	private static final float bulletVelocity = 20f;
	private static final float shootCooldown = 0.1f;

	private ArrayList<Runnable> doNextFrame = new ArrayList<>();
	private final Runnable loseLife = () -> lives--;

	private Timer coolDownTimer;
	private boolean canShoot;
	private Sprite shipBurn;

	private Timer respawnTimer;
	private boolean isActive = true;

	public static int lives = 3;
	public static int score;

	private Vector2 direction;

	public Player(PhysicsEngine engine) {
		this(engine, Vector2.Zero);
	}

	Player(PhysicsEngine engine, Vector2 position) {
		super(engine);
		initialize(Sprites.PLAYER_SPRITE, CollisionHandler.LAYER_PLAYER);

		shipBurn = new Sprite(assets.get(Sprites.PLAYER_BURN, Texture.class));
		shipBurn.setOriginCenter();

		direction = new Vector2(0, 1);
		body.setTransform(position, body.getAngle());
		body.setLinearDamping(0.3f);

		respawnTimer = null;
		lives = 3;
		setActive(true);
	}

	@Override
	public void update() {
		if(!body.isActive())
			return;

		for(int i = 0; i < doNextFrame.size(); i++){
			doNextFrame.get(i).run();
			doNextFrame.remove(i);
		}

		// Thrusting
		direction.setAngle(body.getAngle() * MathUtils.radiansToDegrees + 90);

		if (Input.keyDown(Input.UP)) {
			Sounds.play(Sounds.PLAYER_THRUST);

			Vector2 thrust = new Vector2(direction).nor().scl(speed);
			body.applyForceToCenter(thrust, true);
		}

		body.setLinearVelocity(body.getLinearVelocity().clamp(0, maxSpeed));

		// Rotation
		body.setAngularVelocity(0);
		if (Input.keyDown(Input.LEFT)) body.applyTorque(turnSpeed, true);
		else if (Input.keyDown(Input.RIGHT)) body.applyTorque(-turnSpeed, true);

		//Shooting
		if (canShoot) {
			if(Input.keyPressed(Input.SPACE)) {
				Sounds.play(Sounds.PLAYER_SHOOT);

				Vector2 vel = new Vector2(direction).nor();
				new Bullet(engine, Bullet.BulletType.PLAYER, vel.scl(bulletVelocity), body.getPosition());
				canShoot = false;
			}
		}else{
			if(coolDownTimer == null)
				coolDownTimer = Timer.startNew(shootCooldown, this::resetCoolDownTimer);
		}

		// Hyperjump
		if (Input.keyPressed(Input.LSHIFT) && isActive)
			hyperJump();

		wrap();
	}

	private void resetCoolDownTimer(){
		canShoot = true;
		coolDownTimer = null;
	}

	@Override
	public void draw(SpriteBatch batch, AssetManager manager) {
		if(respawnTimer != null && respawnTimer.isRunning() && (MathUtils.round(respawnTimer.progress() * 10)) % 2f == 0) // respawn blinking
			return;
		else if(respawnTimer != null && !respawnTimer.isRunning() && !isActive) // hyperdrift
			return;

		Vector2 pos = body.getPosition();
		batch.draw(getSprite(), pos.x - sprite.getOriginX(), pos.y - sprite.getOriginY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), 1 / Sprites.PIXELS_PER_METER, 1 / Sprites.PIXELS_PER_METER, body.getAngle() * MathUtils.radiansToDegrees);
	}

	private Sprite getSprite(){
		if(Input.keyDown(Input.UP)){
			if (engine.elapsedTicks % 3 == 0)
				return shipBurn;
			return sprite;
		}else{
			return sprite;
		}
	}

	@Override
	public void delete(){
		super.delete();
		if(coolDownTimer != null)
			coolDownTimer.clear();
	}

	public void loseLife(){
		if(!(engine.game.getScreen() instanceof GameScreen))
			return;

		if(!doNextFrame.contains(loseLife)) {
			doNextFrame.add(loseLife);

			Sounds.play(Sounds.PLAYER_DEATH);

			if(lives - 1 <= 0)
				return;

			doNextFrame.add(() -> {
				body.setTransform(new Vector2(0, 0), 0f);
				body.setLinearVelocity(new Vector2(0, 0));
			});

			setActive(false);
			respawnTimer = Timer.startNew(2f, () -> setActive(true));
		}
	}

	private void hyperJump(){
		Sounds.play(Sounds.PLAYER_JUMP);
		setActive(false);
		Timer.startNew(0.5f, () -> {
			setPosition(randomPosition());
			setActive(true);
		});
	}

	private void setActive(boolean active){
		isActive = active;
		CollisionHandler.playerInvincible = !active;
	}

	public void gameOver(){
		setActive(false);
		body.setActive(false);
	}

	Vector2 getPosition(){
		return body.getPosition();
	}
}
