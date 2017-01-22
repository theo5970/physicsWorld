package com.theo5970.physicsworld.component;

import com.badlogic.gdx.graphics.Color;
import com.theo5970.physicsworld.IDisposable;

public class ColorComponent implements Component, IDisposable {
    public Color color;
    public ColorComponent(Color color) {
        this.color = color;
    }

    @Override
    public void dispose () {
        this.color = null;
    }
}
