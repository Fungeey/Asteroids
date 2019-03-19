package game.asteroids;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public abstract class Entity {
	private static final ArrayList<Entity> entities = new ArrayList<Entity>();

    Vector2 position;
    float rotation;
    Body body;

    Entity(World world){
    	entities.add(this);

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		this.body = world.createBody(def);
		this.body.setUserData(this);
	}

	public static void updateEntities(){
		for (Entity entity : entities) {
			entity.update();
		}
	}

	public abstract void update();
}
