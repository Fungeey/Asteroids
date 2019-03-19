package game.asteroids;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Asteroid extends Entity implements Destructable {
    AsteroidSize size;

    public Asteroid(AsteroidSize size, World world, BodyEditorLoader loader) {
        super(world);

        FixtureDef fix = new FixtureDef();
        fix.density = 10;
        fix.friction = 0.5f;
        fix.restitution = 0.3f;

        switch (size) {
            case SMALL:
                loader.attachFixture(body, "asteroid_small.png", fix, 1);
                break;
            case MEDIUM:
                loader.attachFixture(body, "asteroid_medium.png", fix, 1);
                break;
            case LARGE:
                loader.attachFixture(body, "asteroid_large.png", fix, 1);
                break;
        }
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
}
