package game.asteroids.entities;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Helper class that defines what happens when two Entities collide based on their class type
 */
public class AsteroidsCollisionListener implements ContactListener {
	public void beginContact(Contact contact){
		Entity a = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity b = (Entity) contact.getFixtureB().getBody().getUserData();

		if((a.layer | b.layer) == (Entity.LAYER_ASTEROIDS | Entity.LAYER_PLAYER)){
			if(a.layer == Entity.LAYER_ASTEROIDS) a.delete();
			else b.delete();
		}else if((a.layer | b.layer) == (Entity.LAYER_PLAYER_BULLET | Entity.LAYER_ASTEROIDS)){
			a.delete();
			b.delete();
		}
	}

	// Player - Asteroid
	// Player bullet - Asteroid

	public void endContact(Contact contact) {}

	public void preSolve(Contact contact, Manifold oldManifold){}

	public void postSolve(Contact contact, ContactImpulse impulse){}
}
