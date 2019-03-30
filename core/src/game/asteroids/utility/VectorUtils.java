package game.asteroids.utility;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Contains code to deal with vector math.
 */
public final class VectorUtils {
    /**
     * Converts a Vector3 to a Vector2, discarding the z-value
     *
     * @param v Vector3 to convert
     * @return converted Vector2
     */
    public static Vector2 V3toV2 (Vector3 v) {
        return new Vector2(v.x, v.y);
    }
}
