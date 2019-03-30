package systems;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.asteroids.Asteroids;
import game.asteroids.utility.Sprites;

import java.util.ArrayList;

public class Particles {
    static AssetManager manager;
    
    static ParticleEffectPool explosionPool;
    static ParticleEffectPool hyperdrivePool;
    
    static ArrayList<ParticleEffectPool.PooledEffect> effects = new ArrayList<>();
    
    public static void initialize(Asteroids game) {
        manager = game.manager;
        loadParticleEffects();
        
        ParticleEffect explosion = manager.get("particles/explosion.p", ParticleEffect.class);
        explosion.scaleEffect(1/Sprites.PIXELS_PER_METER);
        
        ParticleEffect hyperdrive = manager.get("particles/hyperdrive.p", ParticleEffect.class);
        explosion.scaleEffect(1/Sprites.PIXELS_PER_METER);
        
        explosionPool = new ParticleEffectPool(explosion, 1, 10);
        hyperdrivePool = new ParticleEffectPool(hyperdrive, 1, 2);
    }
    
    public static void explode(float x, float y) {
        ParticleEffectPool.PooledEffect effect = explosionPool.obtain();
        effect.setPosition(x, y);
        effects.add(effect);
    }
    
    public static void hyperdrive(float x, float y) {
        ParticleEffectPool.PooledEffect effect = hyperdrivePool.obtain();
        effect.setPosition(x, y);
        effects.add(effect);
    }
    
    public static void update(SpriteBatch batch, float delta) {
        for (int i = effects.size() - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            effect.draw(batch, delta);
            if (effect.isComplete()) {
                effect.free();
                effects.remove(i);
            }
        }
    }
    
    static void loadParticleEffects() {
        manager.load("particles/explosion.p", ParticleEffect.class);
        manager.load("particles/explosion.png", Texture.class);
        
        manager.load("particles/hyperdrive.p", ParticleEffect.class);
        manager.load("particles/hyperdrive.png", Texture.class);
        manager.finishLoading();
    }
    
    public static void dispose() {
        for (int i = effects.size() - 1; i >= 0; i--)
            effects.get(i).free();
        effects.clear();
    }
}
