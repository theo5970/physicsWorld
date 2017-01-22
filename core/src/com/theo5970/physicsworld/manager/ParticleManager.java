package com.theo5970.physicsworld.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.ObjectMap;

public class ParticleManager {

    private static ObjectMap<String, ParticleEffect> particleMap;
    private static ObjectMap<String, ParticleEffectPool> particlePoolMap;
    static {
        particleMap = new ObjectMap<String, ParticleEffect>();
        particlePoolMap = new ObjectMap<String, ParticleEffectPool>();
    }
    public static void register(String key, String filename) {
        ParticleEffect particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal(filename),
                Gdx.files.internal("assets/"));
        particleEffect.start();
        ParticleEffectPool particleEffectPool = new ParticleEffectPool(particleEffect, 1, 2);
        particlePoolMap.put(key, particleEffectPool);
        particleMap.put(key, particleEffect);

    }

    public static ParticleEffect get(String key) {
        return particleMap.get(key);
    }

    public static ParticleEffectPool getPool(String key) {
        return particlePoolMap.get(key);
    }
    public static void update(float delta) {
        for (ParticleEffect effect : particleMap.values()) {
            effect.update(delta);
        }
    }
}
