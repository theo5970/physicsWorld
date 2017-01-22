package com.theo5970.physicsworld.component;

import com.badlogic.gdx.graphics.Texture;
import com.theo5970.physicsworld.IDisposable;

public class TextureComponent implements Component, IDisposable{
    public Texture texture;
    public TextureRotation textureRotation;
    public TextureComponent(Texture texture) {
        this.texture = texture;
        this.textureRotation = TextureRotation.ZERO;
    }
    public TextureComponent(Texture texture, TextureRotation rotation) {
        this.texture = texture;
        this.textureRotation = rotation;
    }

    @Override
    public void dispose () {
        textureRotation = null;
    }
}
