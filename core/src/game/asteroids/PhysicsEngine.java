package game.asteroids;

import com.badlogic.gdx.physics.box2d.World;

public class PhysicsEngine {
    private static final float timestep = 1/60;

    private float accumulator = 0;
    private final World world;

    public PhysicsEngine(World world) {
        this.world = world;
    }

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= timestep) {
            world.step(timestep, 6, 2);
            accumulator -= timestep;
        }
    }
}
