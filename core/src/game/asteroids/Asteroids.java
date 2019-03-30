package game.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import game.asteroids.screens.GameScreen;
import game.asteroids.screens.HelpScreen;
import game.asteroids.screens.MainScreen;
import systems.GUI;
import systems.Particles;
import systems.Sounds;

/**
 * Main class for the game.
 * The game is called Asteroids, and is based on the game of the same name from 1979.
 * The game is about taking control of a spaceship, which can shoot at asteroids.
 * New asteroids reappear on screen after all are destroyed.
 * Alien saucers appear periodically, which have the ability to shoot at the player.
 */
public class Asteroids extends Game {

    public AssetManager manager;

    @Override
    public void create() {
        Gdx.graphics.setResizable(false);
        manager = new AssetManager();
        Particles.initialize(this);
		this.setScreen(new MainScreen(this));
    }

    @Override
    public void dispose() {
        GUI.dispose();
        Sounds.dispose();
        Particles.dispose();
        manager.dispose();
    }

	@Override
	public void render() {
		super.render();
	}
}
