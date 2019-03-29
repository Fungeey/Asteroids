package game.asteroids.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.asteroids.PhysicsEngine;
import game.asteroids.screens.GameScreen;
import game.asteroids.utility.Sprites;
import game.asteroids.utility.Timer;

public class Saucer extends Entity implements Destructable{
	private static final float startVelocity = 300f;
	private static final float shootCoolDown = 1f;
	public static float accuracy = 0;
	private static final float bulletVelocity = 20f;

	public static Player player;

	private SaucerSize size;
	private boolean toDestroy = false;

	private Timer directionTimer;
	private Timer shootTimer;
	private Timer lifeTimer;

	public Saucer(PhysicsEngine engine, SaucerSize size) {
		super(engine);
		this.size = size;

		if(size == SaucerSize.SMALL && accuracy <= 360)
			accuracy += 5;

		initialize(getSprite(), CollisionHandler.LAYER_SAUCER);
		body.setFixedRotation(true);

		float x = (GameScreen.worldWidth / 2 + GameScreen.buffer) * MathUtils.randomSign();
		float y = MathUtils.random((GameScreen.worldWidth / 2 - GameScreen.buffer)  * MathUtils.randomSign() * MathUtils.random(1));
		body.setTransform(new Vector2(x, y), body.getAngle());
		Vector2 thrust = new Vector2(-x, 0);
		body.applyForceToCenter(thrust.nor().scl(startVelocity), true);

		shootTimer = Timer.startNew(shootCoolDown, this::shoot);
		lifeTimer = Timer.startNew(MathUtils.random(3, 5), () -> player = player);
	}

	private void shoot(){
		shootTimer = Timer.startNew(shootCoolDown, this::shoot);

		if(size == SaucerSize.LARGE) {
			Vector2 bulletDir = new Vector2 (0, 1).setAngle(MathUtils.random(0, 360));
			new Bullet(engine, Bullet.BulletType.SAUCER, bulletDir.nor().scl(bulletVelocity), body.getPosition());
		}else {
			float angle = MathUtils.atan2(player.getPosition().y-body.getPosition().y, player.getPosition().x-body.getPosition().x) * MathUtils.radiansToDegrees;
			angle += ((360-accuracy)/2) * MathUtils.randomSign() * MathUtils.random((360-accuracy)/360);
			Vector2 bulletDir = new Vector2 (0, 1).setAngle(angle);
			new Bullet(engine, Bullet.BulletType.SAUCER, bulletDir.nor().scl(bulletVelocity), body.getPosition());
		}
	}

	@Override
	public void update(){
		if(wrap() && toDestroy) {
			System.out.println("Saucer Self Destruct");
			this.delete();
		}
	}

	private String getSprite() {
		if (size == SaucerSize.SMALL)
			return Sprites.SAUCER_SMALL_SPRITE_1;
		return Sprites.SAUCER_LARGE_SPRITE_1;
	}

	public enum SaucerSize {
		SMALL, LARGE
	}

	@Override
	public int getPointValue() {
		if(engine.isOnDeleteList(this))
			return 0;

		switch (size) {
			case SMALL:
				return 1000;
			case LARGE:
				return 200;
		}
		return 0;
	}

	@Override
	public void onDestroy() {
		shootTimer.clear();
		//directionTimer.clear();
		lifeTimer.clear();
	}
}
