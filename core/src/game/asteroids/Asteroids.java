package game.asteroids;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.asteroids.screens.TestScreen;

public class Asteroids extends Game {

    @Override
    public void create() {
        this.setScreen(new TestScreen(this));
    }
}
