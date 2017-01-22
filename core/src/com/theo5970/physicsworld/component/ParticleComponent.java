package com.theo5970.physicsworld.component;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.theo5970.physicsworld.IDisposable;

public class ParticleComponent implements Component, IDisposable {
    public ParticleEffectPool pool;
    public boolean isOnce;
    public boolean enabled = true;
    public ParticleComponent(ParticleEffectPool pool) {
        this.pool = pool;
    }

    @Override
    public void dispose () {

    }
}
