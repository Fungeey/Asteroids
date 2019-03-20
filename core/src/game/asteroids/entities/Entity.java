package game.asteroids.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import game.asteroids.BodyEditorLoader;
import game.asteroids.utility.Sprites;

import java.util.ArrayList;

public abstract class Entity {
	private static final ArrayList<Entity> entities = new ArrayList<Entity>();

	private static World world;
	private static BodyEditorLoader loader;

	public static void initialize(World _world, BodyEditorLoader _loader){
		world = _world;
		loader = _loader;
	}

    Vector2 position;
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
}
