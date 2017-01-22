package com.theo5970.physicsworld.manager;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.theo5970.physicsworld.GameObject;
import com.theo5970.physicsworld.component.*;
import com.theo5970.physicsworld.controller.PlayerController;

public class ContactManager implements ContactListener {


    @Override
    public void beginContact (Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        GameObject objectA = (GameObject) bodyA.getUserData();
        GameObject objectB = (GameObject) bodyB.getUserData();

        if (objectA.hasComponent(PlayerComponent.class)) {
            solvePlayerAndObject(fixtureA, fixtureB, bodyA, bodyB, objectA, objectB);
        } else if (objectB.hasComponent(PlayerComponent.class)) {
            solvePlayerAndObject(fixtureB, fixtureA, bodyB, bodyA, objectB, objectA);
        }

    }

    private Vector2 getRandomDamageImpulse() {
        return new Vector2(MathUtils.random(-20f, 20f), MathUtils.random(10f));
    }
    private void solvePlayerAndObject(Fixture playerFixture, Fixture objectFixture,
                                      Body playerBody, Body objectBody,
                                      GameObject playerObject, GameObject gameObject) {

        if (playerFixture.isSensor()) {
            PlayerController.startJumpTimer();
            solveSpring(playerBody, gameObject);
        }
        if (gameObject.hasComponent(DamageComponent.class)) {
            Vector2 impulse = new Vector2();
            if (gameObject.hasComponent(BulletComponent.class)) {
                BulletComponent bulletComponent = gameObject.getComponent(BulletComponent.class);
                impulse.set(new Vector2(bulletComponent.velocity).scl(10f));
            } else {
                impulse.set(getRandomDamageImpulse());
            }
            PlayerController.damage(impulse);
        }
        if (gameObject.hasComponent(ItemComponent.class)) {
            ObjectManager.addBeRemoved(gameObject);
        }
    }
    private void solveSpring(Body playerBody, GameObject gameObject) {
        if (!gameObject.hasComponent(SpringComponent.class)) return;
        SpringComponent component = gameObject.getComponent(SpringComponent.class);
        playerBody.applyLinearImpulse(component.springVelocity,
                playerBody.getPosition(), false);
    }
    @Override
    public void endContact (Contact contact) {
    }

    @Override
    public void preSolve (Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve (Contact contact, ContactImpulse impulse) {

    }
}
