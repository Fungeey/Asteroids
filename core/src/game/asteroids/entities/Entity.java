package game.asteroids.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import game.asteroids.BodyEditorLoader;
import game.asteroids.screens.TestScreen;

import java.util.ArrayList;

public abstract class Entity {
	private static final ArrayList<Entity> entities = new ArrayList<Entity>();

	private static World world;
	private static BodyEditorLoader loader;

	public static void initialize(World _world, BodyEditorLoader _loader){
		world = _world;
		loader = _loader;
	}

    //Vector2 position; // Use body.getPosition() instead
    float rotation;
    Body body;

    Entity(String sprite){
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		this.body = world.createBody(def);
		this.body.setUserData(this);

		FixtureDef fix = new FixtureDef();
		fix.density = 10;
		fix.friction = 0.5f;
		fix.restitution = 0.3f;

		loader.attachFixture(body, sprite, fix,1);
		entities.add(this);
	}

	public static void updateEntities(){
		for (Entity entity : entities) {
			entity.update();
		}
	}

	public abstract void update();

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
}
