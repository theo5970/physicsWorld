package com.theo5970.physicsworld.component;

import com.badlogic.gdx.math.Vector2;
import com.theo5970.physicsworld.IDisposable;

public class BulletComponent implements Component, IDisposable {
    public Vector2 velocity;
    public Vector2 target;
    public float speed;
    public float life;

    public BulletComponent(Vector2 target, float speed, float life) {
        this.target = target;
        this.velocity = new Vector2();
        this.speed = speed;
        this.life = life;
    }

    @Override
    public void dispose () {
        target = null;
        life = 0;
    }
}
