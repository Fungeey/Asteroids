package game.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import game.asteroids.entities.Destructable;
import game.asteroids.entities.Entity;

import java.util.ArrayList;

public class PhysicsEngine {
	private static final float timestep = 1 / 60f;
	public final World world;
	public final Game game;
	private final ArrayList<Entity> entities = new ArrayList<>();
	private final ArrayList<Entity> toDelete = new ArrayList<>();
	public long elapsedTicks = 0;
	public float elapsedSeconds = 0;
	private float accumulator = 0;

	public int numAsteroids = 0;
	public boolean saucerPresent = false;

	public PhysicsEngine(World world, Game game) {
		this.world = world;
		this.game = game;
	}

	public void doPhysicsStep(float deltaTime) {
		// fixed time step
		// max frame time to avoid spiral of death (on slow devices)
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= timestep) {
			world.step(timestep, 6, 2);
			accumulator -= timestep;
		}
	}

	public void updateEntities(float delta) {
		elapsedTicks++;
		elapsedSeconds += delta;

		for (int i = 0; i < entities.size(); i++) {
			if(!toDelete.contains(entities.get(i)))
				entities.get(i).update();
		}

		deleteEntities();
	}

	public void drawEntities(SpriteBatch batch, AssetManager manager) {
		for (int i = 0; i < entities.size(); i++) {
			if(!toDelete.contains(entities.get(i)) || !entities.get(i).isVisible)
				entities.get(i).draw(batch, manager);
		}
	}

	private void deleteEntities() {
		for (int i = toDelete.size() - 1; i >= 0; i--) {
			Entity e = toDelete.get(i);

			if (e instanceof Destructable)
				((Destructable) e).onDestroy();

			world.destroyBody(e.body);
			entities.remove(e);
			toDelete.remove(e);
		}
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void deleteEntity(Entity entity) {
		if (!toDelete.contains(entity))
			toDelete.add(entity);
	}

	public boolean isOnDeleteList(Entity e){
		return toDelete.contains(e);
	}
}
