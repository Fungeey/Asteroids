package game.asteroids.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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

import java.util.ArrayList;
import java.util.Random;

import static game.asteroids.utility.Sprites.PIXELS_PER_METER;

public class GameScreen implements Screen {

	public static final float worldWidth = Gdx.graphics.getWidth() / PIXELS_PER_METER / 2;
	public static final float worldHeight = Gdx.graphics.getHeight() / PIXELS_PER_METER / 2;
	public static final float buffer = 0.25f;

	public static final Random rand = new Random();

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private OrthographicCamera GUICamera;
	private Viewport viewport;

	private Timer saucerSpawner;
	private Timer respawnAsteroids;

	private Player player;

	private PhysicsEngine engine;

	private final Asteroids game;
	private final CollisionHandler collisionListener = new CollisionHandler();

	public GameScreen(Asteroids game) {
		this.game = game;
	}

	private ArrayList<Vector2> stars;

	@Override
	public void show() {
		World world = new World(Vector2.Zero, true);
		world.setContactListener(collisionListener);

		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("bodies.json"));
		loadTextures();

		engine = new PhysicsEngine(world, game);

		camera = new OrthographicCamera(worldWidth, worldHeight);
		//camera.position.set(worldWidth / 2, worldHeight / 2, 0);

		float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
		GUICamera = new OrthographicCamera(1024, 1024*aspectRatio);

		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		Entity.initialize(bodyLoader, game.manager);

		//TODO: Decrease saucer time when less asteroids
		saucerSpawner = Timer.startNew(30, this::spawnSaucer);

		new Saucer(engine, Saucer.SaucerSize.LARGE);
		Saucer.player = player = new Player(engine);

		spawnAsteroids();

		stars = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			stars.add(new Vector2(rand.nextFloat() * 20 - 10, rand.nextFloat() * 14 - 7));
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.15f, 0.1f, 0.15f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		engine.doPhysicsStep(delta);
		Input.update();
		Timer.updateTimers(delta);
		engine.updateEntities(delta);

		if(engine.numAsteroids <= 0 && (respawnAsteroids == null || !respawnAsteroids.isRunning()))
			respawnAsteroids = Timer.startNew(2f, this::spawnAsteroids);

		if(Input.keyPressed(Input.ESCAPE))
			new Saucer(engine);

		batch.begin();
		{
			batch.setProjectionMatrix(camera.combined);

			for (Vector2 v : stars)
				batch.draw(game.manager.get(MathUtils.randomSign() == -1 ? Sprites.BULLET_PLAYER : Sprites.BULLET_SAUCER, Texture.class), v.x, v.y, 0.1f, 0.1f);
			stars.remove(0);
			stars.add(new Vector2(rand.nextFloat() * 20 - 10, rand.nextFloat() * 14 - 7));

			engine.drawEntities(batch, game.manager);

			batch.setProjectionMatrix(GUICamera.combined);
			GUI.drawScore(batch);

			if(Player.lives <= 0){
				// Game Over
				player.gameOver();

				GUI.drawText(batch, "Game Over", -125, 100, 1.5f);
				GUI.drawText(batch, "Press enter to continue", -200, -300, 1f);

				if(Input.keyPressed(Input.ENTER)){
					dispose();
					game.setScreen(new MainScreen(game));
				}
			}
		}
		batch.end();
		
		if (Input.keyPressed(Input.LCTRL)) {
			dispose();
			game.setScreen(new MainScreen(game));
		}
	}

	private void spawnAsteroids(){
		for (int i = 0; i < 4; i++) {
			Vector2 position = Entity.randomPosition();
			Vector2 target = player.body.getPosition();
			if(target.sub(position).len() < 3){
				position.sub(target.sub(position).nor().scl(5));
			}
			new Asteroid(engine, Asteroid.AsteroidSize.LARGE, position);
		}
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
