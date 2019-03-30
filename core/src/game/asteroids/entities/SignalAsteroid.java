package game.asteroids.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import game.asteroids.PhysicsEngine;
import game.asteroids.utility.Sprites;

import java.util.Random;

/**
 * A dummy asteroid which fires configurable code when destroyed.
 * Effectively an interactive stand-in for a traditional mouse-click button.
 */
public class SignalAsteroid extends Entity {
    
    private Runnable action;
    
    public SignalAsteroid(PhysicsEngine engine, Vector2 position, Runnable action) {
        super(engine, false, BodyDef.BodyType.KinematicBody);

        initialize(Sprites.ASTEROID_MEDIUM, CollisionHandler.LAYER_SIGNAL);
        body.setTransform(position, body.getAngle());
        this.action = action;
        
        Random random = new Random();
        body.setAngularVelocity((random.nextFloat()*4 + 1) * (random.nextBoolean() ? 1 : -1));
    }

    public void onHit() {
        action.run();
    }
    
    @Override
    public void update() {
    
    }
}
