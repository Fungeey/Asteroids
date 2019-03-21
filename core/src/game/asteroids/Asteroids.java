package game.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import game.asteroids.screens.TestScreen;

public class Asteroids extends Game {

    public AssetManager manager;

    @Override
    public void create() {
        manager = new AssetManager();
        this.setScreen(new TestScreen(this));
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

}
