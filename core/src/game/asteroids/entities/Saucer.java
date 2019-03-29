package game.asteroids.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private Sprite spriteAlt;

	private SaucerSize size;
	private boolean toDestroy = false;

	private Timer directionTimer;
	private Timer shootTimer;
	private Timer lifeTimer;

	public Saucer(PhysicsEngine engine){
		this(engine, SaucerSize.values()[MathUtils.random(0, 1)]);
	}

	public Saucer(PhysicsEngine engine, SaucerSize size) {
		super(engine);
		this.size = size;

		if(size == SaucerSize.SMALL && accuracy <= 360)
			accuracy += 5;

		engine.saucerPresent = true;

		initialize(size == SaucerSize.SMALL ? Sprites.SAUCER_SMALL_SPRITE_1 : Sprites.SAUCER_LARGE_SPRITE_1, CollisionHandler.LAYER_SAUCER);
		this.spriteAlt = new Sprite(assets.get(size == SaucerSize.SMALL ? Sprites.SAUCER_SMALL_SPRITE_2 : Sprites.SAUCER_LARGE_SPRITE_2, Texture.class));
		spriteAlt.setOriginCenter();
		body.setFixedRotation(true);

		float x = (GameScreen.worldWidth / 2 + GameScreen.buffer) * MathUtils.randomSign();
		float y = MathUtils.random((GameScreen.worldWidth / 2 - GameScreen.buffer)  * MathUtils.randomSign() * MathUtils.random(1f));

		if(y > GameScreen.worldWidth/2)
			System.out.println("Saucer Offscreen");

		body.setTransform(new Vector2(x, y), body.getAngle());
		body.applyForceToCenter(new Vector2(-x, 0).nor().scl(startVelocity), true);

		shootTimer = Timer.startNew(shootCoolDown, this::shoot);
		lifeTimer = Timer.startNew(MathUtils.random(3, 5), () -> toDestroy = true);

		directionTimer = Timer.startNew(MathUtils.random(1f, 3f), this::changeDirection);
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

	private void changeDirection(){
		Vector2 vel = body.getLinearVelocity();
		vel.y = 3f * MathUtils.randomSign() * MathUtils.random(1f);
		body.setLinearVelocity(vel);

		directionTimer = Timer.startNew(MathUtils.random(1f, 3f), this::changeDirection);
	}

	@Override
	public void update(){
		if(wrap() && toDestroy)
			this.delete();
	}

	private Sprite getSprite() {
		if((engine.elapsedTicks/10) % 2 == 0)
			return sprite;
		else
			return spriteAlt;
	}

	public enum SaucerSize {
		SMALL, LARGE
	}

	@Override
	public void draw(SpriteBatch batch, AssetManager manager) {
		if (!body.isActive())
			return;

		Vector2 pos = body.getPosition();
		batch.draw(getSprite(), pos.x - sprite.getOriginX(), pos.y - sprite.getOriginY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), 1 / Sprites.PIXELS_PER_METER, 1 / Sprites.PIXELS_PER_METER, body.getAngle() * MathUtils.radiansToDegrees);
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
		directionTimer.clear();
		lifeTimer.clear();

		engine.saucerPresent = false;
	}
}
