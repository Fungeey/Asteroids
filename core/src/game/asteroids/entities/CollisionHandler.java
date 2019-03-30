package game.asteroids.entities;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Helper class that defines what happens when two Entities collide based on their collision mask settings
 */
public class CollisionHandler implements ContactListener {
	//<editor-fold desc="LAYER MASK SETUP">
	static final short LAYER_DEFAULT = 0x0001;
	static final short LAYER_ASTEROIDS = 0x0002;
	static final short LAYER_PLAYER = 0x0004;
	static final short LAYER_PLAYER_BULLET = 0x0008;
	static final short LAYER_SAUCER = 0x0010;
	static final short LAYER_SAUCER_BULLET = 0x0020;
	static final short LAYER_SIGNAL = 0X0040;

	private static final short MASK_DEFAULT = -1;
	private static final short MASK_ASTEROIDS = LAYER_PLAYER | LAYER_SAUCER | LAYER_PLAYER_BULLET | LAYER_SAUCER_BULLET;
	private static final short MASK_PLAYER = LAYER_ASTEROIDS | LAYER_SAUCER | LAYER_SAUCER_BULLET | LAYER_SIGNAL;
	private static final short MASK_PLAYER_BULLET = (LAYER_ASTEROIDS | LAYER_SAUCER | LAYER_SIGNAL);
	private static final short MASK_SAUCER = LAYER_ASTEROIDS | LAYER_PLAYER | LAYER_PLAYER_BULLET;
	private static final short MASK_SAUCER_BULLET = (LAYER_ASTEROIDS | LAYER_PLAYER);
	private static final short MASK_SIGNAL = LAYER_PLAYER | LAYER_PLAYER_BULLET;
	//</editor-fold>

	public static boolean playerInvincible;

	static short getMask(short layer) {
		if (layer == LAYER_DEFAULT) return MASK_DEFAULT;
		if (layer == LAYER_ASTEROIDS) return MASK_ASTEROIDS;
		if (layer == LAYER_PLAYER) return MASK_PLAYER;
		if (layer == LAYER_PLAYER_BULLET) return MASK_PLAYER_BULLET;
		if(layer == LAYER_SAUCER) return MASK_SAUCER;
		if (layer == LAYER_SIGNAL) return MASK_SIGNAL;
		return MASK_SAUCER_BULLET;
	}

	public void beginContact(Contact contact){
		Entity a = (Entity) contact.getFixtureA().getBody().getUserData();
		Entity b = (Entity) contact.getFixtureB().getBody().getUserData();
		
		int combined = a.layer | b.layer;

		if ((combined & LAYER_PLAYER) != 0 && playerInvincible) {
			if (combined == (LAYER_PLAYER | LAYER_SIGNAL)) {
				if (a.layer == LAYER_SIGNAL) ((SignalAsteroid) a).onHit();
				else ((SignalAsteroid) b).onHit();
			}
			return;
		}
		
		if (combined == (LAYER_ASTEROIDS | LAYER_PLAYER)) {
			if (a.layer == LAYER_ASTEROIDS) {
				Player.score += ((Destructable)a).getPointValue();
				a.delete();
				((Player)b).loseLife();
			} else {
				Player.score += ((Destructable)b).getPointValue();
				b.delete();
				((Player)a).loseLife();
			}
		} else if (combined == (LAYER_ASTEROIDS | LAYER_PLAYER_BULLET)) {
			if (a.layer == LAYER_ASTEROIDS) Player.score += ((Destructable) a).getPointValue();
			else Player.score += ((Destructable)b).getPointValue();
			a.delete();
			b.delete();
		}else if(combined == (LAYER_SAUCER | LAYER_PLAYER_BULLET)){
			if (a.layer == LAYER_SAUCER) Player.score += ((Destructable) a).getPointValue();
			else Player.score += ((Destructable)b).getPointValue();
			a.delete();
			b.delete();
		} else if (combined == (LAYER_PLAYER | LAYER_SAUCER_BULLET)) {
		    if (a.layer == LAYER_PLAYER) {
                ((Player) a).loseLife();
                b.delete();
            } else {
                ((Player) b).loseLife();
                a.delete();
            }
        } else if (combined == (LAYER_PLAYER | LAYER_SAUCER)) {
		    if (a.layer == LAYER_PLAYER) {
                ((Player) a).loseLife();
            } else ((Player) b).loseLife();
        } else if (combined == (LAYER_SAUCER | LAYER_ASTEROIDS)) {
		    a.delete();
		    b.delete();
        } else if (combined == (LAYER_ASTEROIDS | LAYER_SAUCER_BULLET)) {
		    a.delete();
		    b.delete();
		} else if (combined == (LAYER_PLAYER_BULLET | LAYER_SIGNAL)) {
			if (a.layer == LAYER_PLAYER_BULLET) {
				a.delete();
				((SignalAsteroid) b).onHit();
			} else {
				b.delete();
				((SignalAsteroid) a).onHit();
			}
		} else if (combined == (LAYER_PLAYER | LAYER_SIGNAL)) {
			if (a.layer == LAYER_SIGNAL) ((SignalAsteroid) a).onHit();
			else ((SignalAsteroid) b).onHit();
		}
	}

	public void endContact(Contact contact) {}

	public void preSolve(Contact contact, Manifold oldManifold){}

	public void postSolve(Contact contact, ContactImpulse impulse){}
}
