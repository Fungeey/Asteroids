package game.asteroids.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import game.asteroids.Asteroids;
import game.asteroids.BodyEditorLoader;
import game.asteroids.PhysicsEngine;
import game.asteroids.entities.Entity;
import game.asteroids.entities.Player;
import game.asteroids.entities.SignalAsteroid;
import game.asteroids.input.Input;
import game.asteroids.utility.Sprites;
import systems.CollisionHandler;
import systems.GUI;
import systems.Sounds;
import systems.Timer;

import static game.asteroids.screens.GameScreen.worldHeight;
import static game.asteroids.screens.GameScreen.worldWidth;

public class DeathScreen implements Screen {
	private final Asteroids game;
	private final CollisionHandler collisionListener = new CollisionHandler();
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private OrthographicCamera GUICamera;
	private PhysicsEngine engine;

	public DeathScreen (Asteroids game) {
		this.game = game;
	}

	@Override
	public void show() {
		World world = new World(Vector2.Zero, true);
		world.setContactListener(collisionListener);
		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("bodies.json"));
		engine = new PhysicsEngine(world, game);

		camera = new OrthographicCamera(worldWidth * 1, worldHeight * 1);

		float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
		GUICamera = new OrthographicCamera(1024, 1024*aspectRatio);

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		loadTextures();
		Entity.initialize(bodyLoader, game.manager);

		new Player(engine);

		new SignalAsteroid(engine, new Vector2(6, -5), () -> {
			Sounds.play(Sounds.GAME_TRANSITION_1);
			dispose();
			game.setScreen(new MainScreen(game));
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		engine.doPhysicsStep(delta);
		Input.update();
		Timer.updateTimers(delta);
		engine.updateEntities(delta);

		batch.begin();
		{
			batch.setProjectionMatrix(camera.combined);
			engine.drawEntities(batch, game.manager);
			for (int x = -10; x < 10; x++) {
				for (int y = -10; y < 10; y++) {
					batch.draw(game.manager.get(Sprites.BULLET_PLAYER, Texture.class), x, y, 0.1f, 0.1f);
				}
			}

			batch.setProjectionMatrix(GUICamera.combined);
			GUI.drawText(batch, "Menu", 360,-245, 1.5f);
		}
		batch.end();



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
		game.manager.clear();
	}

	void loadTextures() {
		game.manager.load(Sprites.ASTEROID_SMALL, Texture.class);
		game.manager.load(Sprites.ASTEROID_MEDIUM, Texture.class);
		game.manager.load(Sprites.ASTEROID_LARGE, Texture.class);

		game.manager.load(Sprites.BULLET_PLAYER, Texture.class);
		game.manager.load(Sprites.BULLET_SAUCER, Texture.class);

		game.manager.load(Sprites.PLAYER_SPRITE, Texture.class);
		game.manager.load(Sprites.PLAYER_BURN, Texture.class);

		game.manager.load(Sprites.SAUCER_LARGE_SPRITE_1, Texture.class);
		game.manager.load(Sprites.SAUCER_LARGE_SPRITE_2, Texture.class);
		game.manager.load(Sprites.SAUCER_SMALL_SPRITE_1, Texture.class);
		game.manager.load(Sprites.SAUCER_SMALL_SPRITE_2, Texture.class);
		game.manager.finishLoading();
	}
}
