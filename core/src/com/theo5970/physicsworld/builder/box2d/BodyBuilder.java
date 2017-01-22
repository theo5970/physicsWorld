package com.theo5970.physicsworld.builder.box2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class BodyBuilder {
    private List<FixtureDef> fixtureDefList;
    private float firstRotation;

    private BodyDef bodyDef;

    {
        fixtureDefList = new ArrayList<FixtureDef>();
        bodyDef = new BodyDef();
    }

    public BodyBuilder setGravityScale(float gravityScale) {
        bodyDef.gravityScale = gravityScale;
        return this;
    }

    public BodyBuilder addFixtureDef (FixtureDef fixtureDef) {
        fixtureDefList.add(fixtureDef);
        return this;
    }

    public BodyBuilder setPosition (Vector2 position) {
        bodyDef.position.set(position);
        return this;
    }

    public BodyBuilder setPosition (float x, float y) {
        bodyDef.position.set(x, y);
        return this;
    }

    public BodyBuilder setBodyType (BodyDef.BodyType bodyType) {
        bodyDef.type = bodyType;
        return this;
    }

    public BodyBuilder setFixedRotation (boolean isFixedRotation) {
        bodyDef.fixedRotation = isFixedRotation;
        return this;
    }

    public BodyBuilder setLinearDamping(float linearDamping) {
        bodyDef.linearDamping = linearDamping;
        return this;
    }

    public BodyBuilder setAngularDamping(float angularDamping) {
        bodyDef.angularDamping = angularDamping;
        return this;
    }

    public BodyBuilder setRotation(float rotation) {
        firstRotation = rotation;
        return this;
    }

    public Body build (World world) {
        Body body = world.createBody(bodyDef);
        body.setTransform(body.getPosition(), firstRotation * MathUtils.degreesToRadians);

        for (FixtureDef fixtureDef : fixtureDefList) {
            body.createFixture(fixtureDef);
            fixtureDef.shape.dispose();
        }
        fixtureDefList.clear();
        bodyDef = null;
        fixtureDefList = null;
        return body;
    }
}
