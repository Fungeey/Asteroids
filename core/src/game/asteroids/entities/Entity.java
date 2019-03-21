package game.asteroids.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import game.asteroids.BodyEditorLoader;
import game.asteroids.screens.TestScreen;
import game.asteroids.utility.Sprites;

import java.util.ArrayList;

public abstract class Entity {
	private static final ArrayList<Entity> entities = new ArrayList<Entity>();

	//<editor-fold desc="LAYER MASK SETUP">
	static final short LAYER_DEFAULT = 0x0001;
	static final short LAYER_ASTEROIDS = 0x002;
	static final short LAYER_PLAYER = 0x0004;
	static final short LAYER_PLAYER_BULLET = 0x0008;

	private static final short MASK_DEFAULT = -1; // Collide with everything
	private static final short MASK_ASTEROIDS = LAYER_PLAYER | LAYER_PLAYER_BULLET;
	private static final short MASK_PLAYER = LAYER_ASTEROIDS;
	private static final short MASK_PLAYER_BULLET = LAYER_ASTEROIDS;
	//</editor-fold>

	private static World world;
	private static BodyEditorLoader loader;
	private static AssetManager assets;
	protected Sprite sprite;

	float rotation;
	Body body;
	String spriteID;

	public static void initialize(World _world, BodyEditorLoader _loader, AssetManager _assets) {
		world = _world;
		loader = _loader;
		assets = _assets;
	}

    Entity(){
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		this.body = world.createBody(def);
		this.body.setUserData(this);

		entities.add(this);
	}

	Entity(boolean fixedRotation){
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.fixedRotation = true;
		this.body = world.createBody(def);
		this.body.setUserData(this);

		entities.add(this);
	}

	public static void drawEntities(SpriteBatch batch, AssetManager manager) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).draw(batch, manager);
		}
	}

	void initialize(String sprite){
		_initialize(sprite, new CircleShape(), LAYER_DEFAULT, true);
	}

	void initialize(String sprite, short layer){
		_initialize(sprite, new CircleShape(), layer, true);
	}

	void initialize(String sprite, Shape shape){
		_initialize(sprite, shape, LAYER_DEFAULT, false);
	}

	void initialize(String sprite, Shape shape, short layer){
		_initialize(sprite, shape, layer, false);
	}


	private FixtureDef getDefaultFixture(){
		return getDefaultFixture(LAYER_DEFAULT);
	}

	private FixtureDef getDefaultFixture(short layer){
		FixtureDef fix = new FixtureDef();
		fix.density = 10;
		fix.friction = 0.5f;
		fix.restitution = 0.3f;

		fix.filter.categoryBits = layer;
		fix.filter.maskBits = getMask(layer);

		return fix;
	}

	private short getMask(short layer){
    	if(layer == LAYER_DEFAULT)
    		return MASK_DEFAULT;
		if(layer == LAYER_ASTEROIDS)
			return MASK_ASTEROIDS;
		if(layer == LAYER_PLAYER)
			return MASK_PLAYER;
		return MASK_PLAYER_BULLET;
	}



	public static void updateEntities(){
		for(int i = 0; i < entities.size(); i++){
			entities.get(i).update();
		}
	}

	private void _initialize(String spriteID, Shape shape, short layer, boolean useLoader) {
		this.spriteID = spriteID;
		this.sprite = new Sprite(assets.get(this.spriteID, Texture.class));
		sprite.setOriginCenter();

		if (useLoader) {
			loader.attachFixture(body, spriteID, getDefaultFixture(layer), sprite.getWidth() / Sprites.PIXELS_PER_METER);
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

    void wrap(){
		float buffer = 0.25f;
    	float w = TestScreen.worldWidth/2 + buffer;
    	float h = TestScreen.worldHeight/2 + buffer;

		Vector2 pos = body.getPosition();
		if(pos.x < -w)
			setPosition(w, pos.y);
		else if(pos.x > w)
			setPosition(-w, pos.y);
		if(pos.y < -h)
			setPosition(pos.x, h);
		else if(pos.y > h)
			setPosition(pos.x, -h);
	}

	void setPosition(float x, float y){
		setPosition(new Vector2(x, y));
	}

	void setPosition(Vector2 position){
		body.setTransform(position, body.getAngle());
	}

	public static Vector2 randomPosition(){
    	float w = TestScreen.worldWidth;
    	float h = TestScreen.worldHeight;
    	return new Vector2(TestScreen.rand.nextFloat()*w-w/2, TestScreen.rand.nextFloat()*h-h/2);
	}

	public void delete() {
		body.getWorld().destroyBody(body);
		entities.remove(this);
	}
}
