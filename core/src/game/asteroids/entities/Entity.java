package game.asteroids.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import game.asteroids.BodyEditorLoader;
import game.asteroids.PhysicsEngine;
import game.asteroids.screens.GameScreen;
import game.asteroids.utility.Sprites;

/**
 * Generic game entity with physics capabilities and a sprite to draw.
 */
public abstract class Entity {
	private static BodyEditorLoader loader;
	static AssetManager assets;
	public Body body;
	PhysicsEngine engine;
	Sprite sprite;
	private String spriteID;
	short layer;

	Entity(PhysicsEngine engine) {
		this(engine, false);
	}

	Entity(PhysicsEngine engine, boolean fixedRotation) {
		this.engine = engine;
		engine.addEntity(this);

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.fixedRotation = fixedRotation;
		this.body = engine.world.createBody(def);
		this.body.setUserData(this);
	}

	Entity(PhysicsEngine engine, boolean fixedRotation, BodyDef.BodyType bodyType) {
		this.engine = engine;
		engine.addEntity(this);

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.type = bodyType;
		def.fixedRotation = fixedRotation;
		this.body = engine.world.createBody(def);
		this.body.setUserData(this);
	}

	public static void initialize(BodyEditorLoader _loader, AssetManager _assets) {
		loader = _loader;
		assets = _assets;
	}

	void initialize(String sprite, short layer) {
		_initialize(sprite, new CircleShape(), layer, true);
	}

	void initialize(String sprite, Shape shape, short layer) {
		_initialize(sprite, shape, layer, false);
	}

	private FixtureDef getDefaultFixture() {
		return getDefaultFixture(CollisionHandler.LAYER_DEFAULT);
	}

	private FixtureDef getDefaultFixture(short layer) {
		FixtureDef fix = new FixtureDef();
		fix.density = 10;
		fix.friction = 0.5f;
		fix.restitution = 0.3f;

		fix.filter.categoryBits = layer;
		fix.filter.maskBits = CollisionHandler.getMask(layer);

		return fix;
	}

	private void _initialize(String spriteID, Shape shape, short layer, boolean useLoader) {
		this.layer = layer;
		this.spriteID = spriteID;
		this.sprite = new Sprite(assets.get(this.spriteID, Texture.class));
		sprite.setOriginCenter();

		if (useLoader) {
			loader.attachFixture(body, spriteID.substring(spriteID.lastIndexOf('/')+1), getDefaultFixture(layer), sprite.getWidth() / Sprites.PIXELS_PER_METER);
		} else {
			FixtureDef fix = getDefaultFixture(layer);
			fix.shape = shape;
			body.createFixture(fix);
		}

		shape.dispose();
	}

	public abstract void update();

	public void draw(SpriteBatch batch, AssetManager manager) {
		Vector2 pos = body.getPosition();
		batch.draw(sprite, pos.x - sprite.getOriginX(), pos.y - sprite.getOriginY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), 1 / Sprites.PIXELS_PER_METER, 1 / Sprites.PIXELS_PER_METER, body.getAngle() * MathUtils.radiansToDegrees);
	}

	public static Vector2 randomPosition() {
		float w = GameScreen.worldWidth;
		float h = GameScreen.worldHeight;
		return new Vector2(GameScreen.rand.nextFloat() * w - w / 2, GameScreen.rand.nextFloat() * h - h / 2);
	}

	void setPosition(float x, float y) {
		setPosition(new Vector2(x, y));
	}

	void setPosition(Vector2 position) {
		body.setTransform(position, body.getAngle());
	}

	boolean wrap() {
		float w = GameScreen.worldWidth / 2 + GameScreen.buffer;
		float h = GameScreen.worldHeight / 2 + GameScreen.buffer;

		Vector2 pos = body.getPosition();
		boolean wrapped = false;
		if (pos.x < -w) {
			setPosition(w, pos.y);
			wrapped = true;
		}else if (pos.x > w) {
			setPosition(-w, pos.y);
			wrapped = true;
		}
		if (pos.y < -h) {
			setPosition(pos.x, h);
			wrapped = true;
		}else if (pos.y > h) {
			setPosition(pos.x, -h);
			wrapped = true;
		}
		return wrapped;
	}

	void delete() {
		engine.deleteEntity(this);
	}
}
