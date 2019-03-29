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
import game.asteroids.utility.Timer;
import game.asteroids.utility.VectorUtils;

import static game.asteroids.screens.GameScreen.worldHeight;
import static game.asteroids.screens.GameScreen.worldWidth;

public class MainScreen implements Screen {
    private final Asteroids game;
    private final CollisionHandler collisionListener = new CollisionHandler();
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera GUICamera;
    private PhysicsEngine engine;
    
    boolean exiting = false;
    
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
        
        float aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        GUICamera = new OrthographicCamera(1024, 1024*aspectRatio);
        
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        
        loadTextures();
        Entity.initialize(bodyLoader, game.manager);
        
        
        new Player(engine);
        
        new SignalAsteroid(engine, VectorUtils.V3toV2(camera.unproject(new Vector3(800, 380, 0))), () -> {
            dispose();
            game.setScreen(new GameScreen(game));
        });
        new SignalAsteroid(engine, VectorUtils.V3toV2(camera.unproject(new Vector3(800, 510, 0))), () -> {
            dispose();
            game.setScreen(new HelpScreen(game));
        });
        new SignalAsteroid(engine, VectorUtils.V3toV2(camera.unproject(new Vector3(800, 640, 0))), () -> {
            dispose();
            Gdx.app.exit();
        });
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        engine.doPhysicsStep(delta);
        Input.update();
        Timer.updateTimers(delta);
        engine.updateEntities(delta);
        if (!exiting) {
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
                GUI.drawText(batch, "Asteroids!", -400, 250, 3);
                GUI.drawText(batch, "Start Game", 170, 90, 1.5f);
                GUI.drawText(batch, "Tutorial", 170, -15, 1.5f);
                GUI.drawText(batch, "Quit Game", 170, -115, 1.5f);
                batch.draw(game.manager.get(Sprites.UP_KEY, Texture.class), -400, 30);
                batch.draw(game.manager.get(Sprites.ARROW_KEYS, Texture.class), -400, -50);
                batch.draw(game.manager.get(Sprites.SPACEBAR, Texture.class), -400, -120);
                GUI.drawText(batch, "thrust", -320, 65, 1);
                GUI.drawText(batch, "turn", -250, -15, 1);
                GUI.drawText(batch, "shoot", -250, -85, 1);
                
            }
            batch.end();
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
        exiting = true;
        game.manager.clear();
    }
    
    private void loadTextures() {
        game.manager.load(Sprites.PLAYER_SPRITE, Texture.class);
        game.manager.load(Sprites.PLAYER_BURN, Texture.class);
        game.manager.load(Sprites.BULLET_PLAYER, Texture.class);
        
        game.manager.load(Sprites.ASTEROID_MEDIUM, Texture.class);
        game.manager.load(Sprites.ASTEROID_SMALL, Texture.class);
    
    
        game.manager.load(Sprites.ARROW_KEYS, Texture.class);
        game.manager.load(Sprites.UP_KEY, Texture.class);
        game.manager.load(Sprites.SPACEBAR, Texture.class);
        game.manager.finishLoading();
    }
}
