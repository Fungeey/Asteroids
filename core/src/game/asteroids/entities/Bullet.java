package game.asteroids.entities;

import com.badlogic.gdx.math.Vector2;
import game.asteroids.utility.Sprites;

public class Bullet extends Entity{
	private BulletType type;

	public Bullet(BulletType type, Vector2 velocity){
		super(type == BulletType.PLAYER ? Sprites.ASTEROID_SMALL : Sprites.ASTEROID_SMALL);
		this.type = type;

		body.applyForceToCenter(velocity, true);
	}

	public enum BulletType {
		PLAYER, SAUCER
	}

	public void update(){}
}
