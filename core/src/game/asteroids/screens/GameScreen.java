package game.asteroids.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import game.asteroids.Asteroids;
import game.asteroids.BodyEditorLoader;
import game.asteroids.PhysicsEngine;
import game.asteroids.entities.*;
import game.asteroids.input.Input;
import game.asteroids.utility.Sprites;
import game.asteroids.utility.Timer;

import java.util.Random;

import static game.asteroids.utility.Sprites.PIXELS_PER_METER;

public class GameScreen implements Screen {

	public static final float worldWidth = Gdx.graphics.getWidth() / PIXELS_PER_METER / 3;
	public static final float worldHeight = Gdx.graphics.getHeight() / PIXELS_PER_METER / 3;
	public static final float buffer = 0.25f;

	public static final Random rand = new Random();

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private OrthographicCamera GUICamera;
	private Viewport viewport;

	private Timer saucerSpawner;
	private Timer respawnAsteroids;

	private PhysicsEngine engine;

	private final Asteroids game;
	private final CollisionHandler collisionListener = new CollisionHandler();

	public GameScreen(Asteroids game) {
		this.game = game;
	}

	@Override
	public void show() {
		Gdx.graphics.setResizable(false);
		World world = new World(Vector2.Zero, true);
		world.setContactListener(collisionListener);

		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("bodies.json"));
		loadTextures();

		engine = new PhysicsEngine(world);

		camera = new OrthographicCamera(worldWidth, worldHeight);
		camera.position.set(worldWidth / 2, worldHeight / 2, 0);

		float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
		GUICamera = new OrthographicCamera(1024, 1024*aspectRatio);

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		Random rand = new Random();

		Entity.initialize(bodyLoader, game.manager);

		spawnAsteroids();

		//TODO: Decrease saucer time when less asteroids
		saucerSpawner = Timer.startNew(30, this::spawnSaucer);

		new Saucer(engine, Saucer.SaucerSize.LARGE);
		Saucer.player = new Player(engine);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		engine.doPhysicsStep(delta);
		Input.update();
		Timer.updateTimers(delta);
		engine.updateEntities(delta);

		if(engine.numAsteroids <= 0)
			spawnAsteroids();

		batch.begin();
		{
			batch.setProjectionMatrix(camera.combined);
			engine.drawEntities(batch, game.manager);

			batch.setProjectionMatrix(GUICamera.combined);
			GUI.drawScore(batch);
		}
		batch.end();
		
		if (Input.keyPressed(Input.LCTRL)) {
			dispose();
			game.setScreen(new MainScreen(game));
		}
	}

	private void spawnAsteroids(){
		for (int i = 0; i < 4; i++)
			new Asteroid(engine, Asteroid.AsteroidSize.LARGE, Entity.randomPosition());
	}

	private void spawnSaucer(){
		saucerSpawner = Timer.startNew(30, this::spawnSaucer);
		new Saucer(engine);
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
		GUI.dispose();
		Timer.clearAll();
	}

	private void loadTextures() {
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
