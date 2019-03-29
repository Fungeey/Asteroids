package game.asteroids.entities;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Helper class that defines what happens when two Entities collide based on their class type
 */
public class CollisionHandler implements ContactListener {
	//<editor-fold desc="LAYER MASK SETUP">
	static final short LAYER_DEFAULT = 0x0001;
	static final short LAYER_ASTEROIDS = 0x002;
	static final short MASK_PLAYER_BULLET = LAYER_ASTEROIDS;
	static final short MASK_PLAYER = LAYER_ASTEROIDS;
	static final short LAYER_PLAYER = 0x0004;
	static final short LAYER_PLAYER_BULLET = 0x0008;
	static final short LAYER_SIGNAL = 0x10;
	static final short MASK_ASTEROIDS = LAYER_PLAYER | LAYER_PLAYER_BULLET;
	static final short MASK_DEFAULT = -1; // Collide with everything
	//</editor-fold>

	public void beginContact(Contact contact){
		Entity a = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity b = (Entity) contact.getFixtureB().getBody().getUserData();

		if ((a.layer | b.layer) == (LAYER_ASTEROIDS | LAYER_PLAYER)) {
			if (a.layer == LAYER_ASTEROIDS) {
				Player.score += ((Asteroid)a).getPointValue();
				a.delete();
			} else {
				Player.score += ((Asteroid)b).getPointValue();
				b.delete();
			}
		} else if ((a.layer | b.layer) == (LAYER_PLAYER_BULLET | LAYER_ASTEROIDS)) {
			if (a.layer == LAYER_ASTEROIDS) Player.score += ((Destructable) a).getPointValue();
			else Player.score += ((Asteroid)b).getPointValue();
			a.delete();
			b.delete();
		}
	}

	public void endContact(Contact contact) {}

	public void preSolve(Contact contact, Manifold oldManifold){}

	public void postSolve(Contact contact, ContactImpulse impulse){}
}
