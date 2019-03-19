package game.asteroids;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Entity {
    Vector2 position;
    float rotation;
    Body body;
}
