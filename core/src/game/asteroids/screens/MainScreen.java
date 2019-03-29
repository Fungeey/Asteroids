package game.asteroids.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import game.asteroids.Asteroids;
import game.asteroids.BodyEditorLoader;
import game.asteroids.PhysicsEngine;
import game.asteroids.entities.CollisionHandler;
import game.asteroids.entities.Entity;
import game.asteroids.entities.Player;
import game.asteroids.entities.SignalAsteroid;
import game.asteroids.input.Input;
import game.asteroids.utility.Sprites;
import game.asteroids.utility.Timer;
import game.asteroids.utility.VectorUtils;

import javax.xml.soap.Text;

import static game.asteroids.screens.GameScreen.worldHeight;
import static game.asteroids.screens.GameScreen.worldWidth;

public class MainScreen implements Screen {
	private final Asteroids game;
	private final CollisionHandler collisionListener = new CollisionHandler();
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private PhysicsEngine engine;
	
	public MainScreen(Asteroids game) {
		this.game = game;
	}

	@Override
	public void show() {
		World world = new World(Vector2.Zero, true);
		world.setContactListener(collisionListener);
		BodyEditorLoader bodyLoader = new BodyEditorLoader(Gdx.files.internal("bodies.json"));
		engine = new PhysicsEngine(world);

		camera = new OrthographicCamera(worldWidth * 1, worldHeight * 1);
		//camera.position.set(worldWidth / 2, worldHeight / 2, 0);
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);

		loadTextures();
		Entity.initialize(bodyLoader, game.manager);
		
		
		new Player(engine);
		new SignalAsteroid(engine, VectorUtils.V3toV2(camera.unproject(new Vector3(385, 200, 0))), () -> game.setScreen(new GameScreen(game)));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0.5f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		engine.doPhysicsStep(delta);
		Input.update();
		Timer.updateTimers(delta);
		engine.updateEntities(delta);

		batch.begin();
		{
			engine.drawEntities(batch, game.manager);
			for (int x = -10; x < 10; x++) {
				for (int y = -10; y < 10; y++) {
					batch.draw(game.manager.get(Sprites.BULLET_PLAYER, Texture.class), x, y, 0.1f, 0.1f);
				}
			}
			Vector3 d = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			batch.draw(game.manager.get(Sprites.ASTEROID_SMALL, Texture.class), d.x, d.y, 1, 1);
		}
		batch.end();


		if (Input.keyPressed(Input.LCTRL)) {
			game.setScreen(new GameScreen(game));
		}
		
		if (Gdx.input.justTouched()) {
			System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());
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

	private void loadTextures() {
		game.manager.load(Sprites.PLAYER_SPRITE, Texture.class);
		game.manager.load(Sprites.PLAYER_BURN, Texture.class);
		game.manager.load(Sprites.BULLET_PLAYER, Texture.class);
		game.manager.load(Sprites.ASTEROID_MEDIUM, Texture.class);
		game.manager.load(Sprites.ASTEROID_SMALL, Texture.class);
		game.manager.finishLoading();
	}
}
