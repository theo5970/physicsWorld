package com.theo5970.physicsworld.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;
import com.theo5970.physicsworld.GameObject;
import com.theo5970.physicsworld.component.*;
import com.theo5970.physicsworld.manager.ObjectManager;
import com.theo5970.physicsworld.manager.ParticleManager;

public class UpdateController {

    public static void update(GameObject gameObject) {
        if (gameObject.hasComponent(LifeComponent.class)) {
            LifeComponent lifeComponent = gameObject.getComponent(LifeComponent.class);
            lifeComponent.life -= Gdx.graphics.getDeltaTime();
            if (lifeComponent.life <= 0) {
                ObjectManager.addBeRemoved(gameObject);
            }
        }
        processLift(gameObject);
        processBullet(gameObject);
        processFollow(gameObject);
        processBodyTransform(gameObject);
        processSmoothMove(gameObject);
    }

    private static void processFollow(GameObject gameObject) {
        if (!gameObject.hasComponent(FollowComponent.class)) return;
        FollowComponent followComponent = gameObject.getComponent(FollowComponent.class);

        if (followComponent.following.isDisposed()) return;
        TransformComponent transform = gameObject.getComponent(TransformComponent.class);
        TransformComponent targetTransform = followComponent.following.getComponent(TransformComponent.class);


        transform.position.set(targetTransform.position);
    }
    private static void processSmoothMove(GameObject gameObject) {
        if (!gameObject.hasComponent(SmoothMoveComponent.class)) return;

        SmoothMoveComponent smoothMoveComponent = gameObject.getComponent(SmoothMoveComponent.class);

        float target = (smoothMoveComponent.reversed ? -1 : 1) * smoothMoveComponent.amount;
        smoothMoveComponent.pos += (target - smoothMoveComponent.pos) * 0.05f;
        if (Math.abs(target - smoothMoveComponent.pos) <= 0.1f) {
            smoothMoveComponent.reversed = !smoothMoveComponent.reversed;
        }

        Body body = gameObject.getComponent(BodyComponent.class).getBody();
        body.setTransform(body.getPosition().add(0, smoothMoveComponent.pos * 0.02f), body.getAngle());
    }
    private static void processLift(GameObject gameObject) {
        if (!gameObject.hasComponent(LiftComponent.class)) return;
        LiftComponent liftComponent = gameObject.getComponent(LiftComponent.class);
        Body body = gameObject.getComponent(BodyComponent.class).getBody();

        Vector2 position = body.getPosition();
        Vector2 startPoint = liftComponent.startPoint;
        Vector2 endPoint = liftComponent.endPoint;

        if (position.x > endPoint.x || position.x < startPoint.x) {
            liftComponent.velocity.scl(-1, 1);
        }
        if (position.y > endPoint.y || position.y < startPoint.y) {
            liftComponent.velocity.scl(1, -1);
        }

        Vector2 added = position.add(liftComponent.velocity);
        body.setTransform(added, 0);
    }
    private static void processBullet(GameObject gameObject) {
        if (!gameObject.hasComponent(BulletComponent.class)) return;
        BulletComponent bulletComponent = gameObject.getComponent(BulletComponent.class);
        bulletComponent.life -= Gdx.graphics.getDeltaTime();
        if (bulletComponent.life < 0) {
            ObjectManager.addBeRemoved(gameObject);
            return;
        }
        Body body = gameObject.getComponent(BodyComponent.class).getBody();
        Vector2 diff = new Vector2(bulletComponent.target).sub(body.getPosition());
        if (diff.len() < 1.414f) {
            ObjectManager.addBeRemoved(gameObject);
            return;
        }
        bulletComponent.velocity.set(diff.nor().scl(bulletComponent.speed));

        float angle = body.getAngle() * MathUtils.radiansToDegrees - 8f;
        body.setTransform(body.getPosition().add(bulletComponent.velocity),
                angle * MathUtils.degreesToRadians);

    }
    private static void processBodyTransform(GameObject gameObject) {
        if (!gameObject.hasComponent(BodyComponent.class)) return;
        BodyComponent bodyComponent = gameObject.getComponent(BodyComponent.class);
        TransformComponent transformComponent = gameObject.getComponent(TransformComponent.class);

        Body body = bodyComponent.getBody();
        transformComponent.position.set(body.getPosition(),
                transformComponent.position.z);
        transformComponent.degrees = body.getAngle() * MathUtils.radiansToDegrees;
    }
}
