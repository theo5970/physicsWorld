package com.theo5970.physicsworld.builder.box2d;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public class FixtureDefBuilder {
    public static FixtureDef build (Shape shape, boolean isSensor, float density, float restitution, float friction) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;
        fixtureDef.shape = shape;
        fixtureDef.isSensor = isSensor;
        return fixtureDef;
    }
}
