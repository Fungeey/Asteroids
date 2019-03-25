package game.asteroids.entities;

/**
 * An interface that describes Entities that can be destroyed and increase your score
 */
public interface Destructable {
    int getPointValue();

    void onDestroy();
}
