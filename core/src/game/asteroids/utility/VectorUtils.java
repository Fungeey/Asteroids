package game.asteroids.utility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public final class VectorUtils {
    public static Vector2 V3toV2 (Vector3 v) {
        return new Vector2(v.x, v.y);
    }
}
