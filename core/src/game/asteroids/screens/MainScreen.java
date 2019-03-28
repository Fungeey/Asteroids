package game.asteroids.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import game.asteroids.Asteroids;
import game.asteroids.PhysicsEngine;
import game.asteroids.entities.CollisionHandler;
import game.asteroids.input.Input;

public class MainScreen implements Screen {
	private final Asteroids game;
	private final CollisionHandler collisionListener = new CollisionHandler();
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private PhysicsEngine engine;
	private World world;

	public MainScreen(Asteroids game) {
		this.game = game;
	}

	@Override
	public void show() {
		world = new World(Vector2.Zero, true);
		world.setContactListener(collisionListener);
		engine = new PhysicsEngine(world);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0.5f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		engine.doPhysicsStep(delta);

		Input.update();

		if (Input.keyPressed(Input.LCTRL)) {
			game.setScreen(new GameScreen(game));
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
