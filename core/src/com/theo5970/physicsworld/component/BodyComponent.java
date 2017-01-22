package com.theo5970.physicsworld.component;

import com.badlogic.gdx.physics.box2d.Body;
import com.theo5970.physicsworld.IDisposable;

public class BodyComponent implements Component, IDisposable {
    private Body body;

    public BodyComponent (Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void dispose () {
        body.getWorld().destroyBody(body);
    }
}
