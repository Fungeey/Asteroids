package game.asteroids.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import game.asteroids.*;
import game.asteroids.entities.Asteroid;
import game.asteroids.entities.Entity;
import game.asteroids.entities.Player;
import game.asteroids.input.Input;

public class TestScreen implements Screen {

    public static final float PIXELS_PER_METER = 32;
    public static final float worldWidth = Gdx.graphics.getWidth() / PIXELS_PER_METER;
    public static final float worldHeight = Gdx.graphics.getHeight() / PIXELS_PER_METER;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private PhysicsEngine engine;
    private World world;
    private Box2DDebugRenderer debug;

    public BodyEditorLoader loader;

    private final Asteroids game;

    private Player player;

    public TestScreen(Asteroids game) {
        this.game = game;
    }

    @Override
    public void show() {
        world = new World(Vector2.Zero, true);
        loader = new BodyEditorLoader(Gdx.files.internal("bodies.json"));

        engine = new PhysicsEngine(world);
        debug = new Box2DDebugRenderer();
        camera = new OrthographicCamera(worldWidth, worldHeight);
        camera.position.set(worldWidth / 2, worldHeight / 2, 0);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        Entity.initialize(world, loader);

        new Asteroid(Asteroid.AsteroidSize.MEDIUM);
        new Asteroid(Asteroid.AsteroidSize.SMALL);
        new Asteroid(Asteroid.AsteroidSize.LARGE);

		player = new Player();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debug.render(world, camera.combined);
        engine.doPhysicsStep(delta);

		Input.update();
		Entity.updateEntities();
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
