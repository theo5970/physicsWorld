package com.theo5970.physicsworld.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.theo5970.physicsworld.IDisposable;

public class TransformComponent implements Component, IDisposable{
    public Vector3 position;
    public Vector2 size;
    public Vector2 scale;
    public float degrees;

    public TransformComponent() {
        position = new Vector3();
        size = new Vector2();
        scale = new Vector2(1, 1);
    }

    @Override
    public void dispose () {
        position = null;
        size = null;
        scale = null;
        degrees = 0;
    }

}
