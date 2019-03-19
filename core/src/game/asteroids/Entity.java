package game.asteroids;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity {
    Vector2 position;
    float rotation;
    Body body;

    public Entity(World world){
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		this.body = world.createBody(def);
		this.body.setUserData(this);
	}
}
