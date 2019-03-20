package game.asteroids.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.asteroids.screens.TestScreen;
import game.asteroids.utility.Sprites;

import java.util.Random;

public class Asteroid extends Entity implements Destructable {
    private AsteroidSize size;

    public Asteroid(AsteroidSize size) {
        super(size == AsteroidSize.SMALL ? Sprites.ASTEROID_SMALL : size == AsteroidSize.MEDIUM ? Sprites.ASTEROID_MEDIUM : Sprites.ASTEROID_LARGE);
        this.size = size;
    }

	public Asteroid(AsteroidSize size, Vector2 position) {
		super(size == AsteroidSize.SMALL ? Sprites.ASTEROID_SMALL : size == AsteroidSize.MEDIUM ? Sprites.ASTEROID_MEDIUM : Sprites.ASTEROID_LARGE);
		this.size = size;
		body.setTransform(position, body.getAngle());
	}

    private String getSprite(AsteroidSize size){
        if(size == AsteroidSize.SMALL)
            return Sprites.ASTEROID_SMALL;
        if(size == AsteroidSize.MEDIUM)
            return Sprites.ASTEROID_MEDIUM;
        return Sprites.ASTEROID_LARGE;
    }

    @Override
    public int getPointValue() {
        switch (size) {
            case SMALL:
                return 100;
            case MEDIUM:
                return 50;
            case LARGE:
                return 20;
        }
        return 0;
    }

    public enum AsteroidSize {
        SMALL, MEDIUM, LARGE
    }

    public void update(){
    	wrap();
	}
}
