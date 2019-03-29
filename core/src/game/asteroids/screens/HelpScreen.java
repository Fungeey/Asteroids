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
import game.asteroids.entities.*;
import game.asteroids.input.Input;
import game.asteroids.utility.Sprites;
import systems.CollisionHandler;
import systems.GUI;
import systems.Timer;

import static game.asteroids.screens.GameScreen.worldHeight;
import static game.asteroids.screens.GameScreen.worldWidth;

public class HelpScreen implements Screen {
    private final Asteroids game;
    private final CollisionHandler collisionListener = new CollisionHandler();
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera GUICamera;
    private PhysicsEngine engine;
    
    public HelpScreen (Asteroids game) {
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

        Player player = new Player(engine);
    
        new SignalAsteroid(engine, new Vector2(6, -5), () -> {
            dispose();
            game.setScreen(new MainScreen(game));
        });

        new Asteroid(engine, Asteroid.AsteroidSize.SMALL, new Vector2(-2f, 1.5f), Vector2.Zero);
        new Asteroid(engine, Asteroid.AsteroidSize.MEDIUM, new Vector2(-0.2f, 1.5f), Vector2.Zero);
        new Asteroid(engine, Asteroid.AsteroidSize.LARGE, new Vector2(1.8f, 1.5f), Vector2.Zero);

        Saucer.player = player;
        new Saucer(engine, Saucer.SaucerSize.LARGE).body.setTransform(5, -1, 0);
        new Saucer(engine, Saucer.SaucerSize.SMALL).body.setTransform(8, -1, 0);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        engine.doPhysicsStep(delta);
        Input.update();
        Timer.updateTimers(delta);
        engine.updateEntities(delta);

        if(engine.numAsteroids == 0){
            Timer.startNew(2f, () -> {
                new Asteroid(engine, Asteroid.AsteroidSize.SMALL, new Vector2(-2f, 1.5f), Vector2.Zero);
                new Asteroid(engine, Asteroid.AsteroidSize.MEDIUM, new Vector2(-0.2f, 1.5f), Vector2.Zero);
                new Asteroid(engine, Asteroid.AsteroidSize.LARGE, new Vector2(1.8f, 1.5f), Vector2.Zero);
            });
        }

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

            GUI.drawText(batch, "Controls:", -450, 340, 1f);
            GUI.drawText(batch, "Asteroids:", -120, 340, 1f);
            GUI.drawText(batch, "Saucers:", 220, 340, 1f);

            GUI.drawText(batch,
                    "The basic controls are:\n" +
                            "up arrow to thrust\n" +
                            "left and right arrow to turn\n" +
                            "space to shoot\n\n" +
                            "When objects go off screen, they\n" +
                            "wrap around to the other side.\n\n" +
                            "Your ship is also equipped with\n" +
                            "a hyperdrive which you can activate\n" +
                            "with the shift button.\n" +
                            "However, using it is dangerous as\n" +
                            "you may reenter inside an object!",
                    -500, 300, 0.5f);

            GUI.drawText(batch,
                    "Asteroids come in 3 different sizes.\n" +
                            "Smaller asteroids yield more points.\n\n" +
                            "If you shoot an asteroid, it breaks\n" +
                            "into two smaller asteroids.\n" +
                            "Asteroids don't collide with with\n" +
                            "other asteroids, but will collide\n" +
                            "with and destroy saucers.",
                    -180, 300, 0.5f);

            GUI.drawText(batch,
                    "As the game progresses, saucers appear.\n" +
                            "Saucers can shoot the player and\n" +
                            "destroy asteroids with the bullets\n" +
                            "that they shoot.\n\n" +
                            "The larger saucer is easier to hit,\n" +
                            "and shoots randomly.\n" +
                            "The smaller saucer is more accurate\n" +
                            "and yields more points.",
                    160, 300, 0.5f);

        }
        batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 d = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            System.out.println(d.x + " " + d.y);
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
        game.manager.clear();
        Timer.clearAll();
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
