package game.asteroids.entities;

import game.asteroids.utility.Sprites;

public class Asteroid extends Entity implements Destructable {
    private AsteroidSize size;

    public Asteroid(AsteroidSize size) {
        super(size == AsteroidSize.SMALL ? Sprites.ASTEROID_SMALL : size == AsteroidSize.MEDIUM ? Sprites.ASTEROID_MEDIUM : Sprites.ASTEROID_LARGE);
        this.size = size;
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

    public void update(){}
}
