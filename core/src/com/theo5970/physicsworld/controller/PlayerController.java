package com.theo5970.physicsworld.controller;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.theo5970.physicsworld.GameObject;
import com.theo5970.physicsworld.component.BodyComponent;
import com.theo5970.physicsworld.component.ParticleComponent;
import com.theo5970.physicsworld.component.PlayerComponent;
import com.theo5970.physicsworld.manager.SoundManager;

public class PlayerController {

    private enum JumpState {
        WAIT, JUMPED, JUMPING
    }
    private enum MoveState {
        LEFT, NOP, RIGHT
    }
    private static final Vector2 jumpImpulse = new Vector2(0, 10f);
    private static final Vector2 jumpVector = new Vector2(jumpImpulse);
    private static final float maxJumpVelocity = 30;
    private static float jumpPower = 0;

    private static final float moveAmount = 30;
    private static final float maxMoveVelocity = 30;

    private static MoveState moveState = MoveState.NOP;
    private static JumpState jumpState = JumpState.WAIT;
    private static GameObject playerObject;


    private static final float maxWaitJumpTime = 0.7f;
    private static final float maxWaitDamageTime = 0.1f;

    private static float jumpTimer = 0;
    private static float damageTimer = 0;
    public static void setPlayerObject(GameObject object) {
        playerObject = object;
    }

    public static GameObject getPlayerObject() {
        return playerObject;
    }

    public static Vector2 getPlayerPosition() {
        return playerObject.getComponent(BodyComponent.class).getBody().getPosition();
    }

    public static Body getPlayerBody() {
        return playerObject.getComponent(BodyComponent.class)
                .getBody();
    }
    public static void damage(Vector2 impulse) {
        if (damageTimer < 0) {
            damageTimer = maxWaitDamageTime;
            Body body = getPlayerBody();
            body.applyLinearImpulse(impulse, body.getPosition(), false);
            //SoundManager.play("damage", 0.5f);
        }
    }
    public static void startJumpTimer() {
        jumpTimer = maxWaitJumpTime;
    }
    private static final float minRotation = -90f;
    private static final float maxRotation = 90f;
    private static boolean needRotation = false;
    public static void update() {

        Body body = getPlayerBody();

        if (damageTimer > 0) {
            damageTimer -= Gdx.graphics.getDeltaTime();
        } else {
            damageTimer = -1;
        }

        if (jumpState == JumpState.JUMPING) {
            if (jumpTimer > 0) {
                jumpTimer -= Gdx.graphics.getDeltaTime();
            } else {
                jumpState = JumpState.WAIT;
                jumpPower = 1;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            jumpState = JumpState.JUMPED;
            playerObject.getComponent(ParticleComponent.class).enabled = true;
        }

        playerObject.getComponent(ParticleComponent.class).enabled = jumpState == JumpState.JUMPING;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (jumpState != JumpState.WAIT && jumpTimer > 0) {
                Vector2 velocity = body.getLinearVelocity();
                if (Math.abs(velocity.y) <= maxJumpVelocity) {
                    jumpVector.set(jumpImpulse.x, jumpImpulse.y + jumpPower);
                    jumpVector.scl(body.getGravityScale() > 0 ? 1 : -1);
                    jumpPower += 1;
                    body.applyLinearImpulse(jumpVector, body.getPosition(), false);
                    jumpState = JumpState.JUMPING;
                }
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveState = MoveState.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveState = MoveState.RIGHT;
        } else {
            if (moveState != MoveState.NOP) {
                moveState = MoveState.NOP;
                Vector2 velocity = body.getLinearVelocity();
                body.setLinearVelocity(velocity.x * 0.02f, velocity.y);
            }
        }

        if (moveState != MoveState.NOP) {
            float x = body.getPosition().x;
            float y = body.getPosition().y;
            float impulse = (moveState == MoveState.LEFT) ? -1 : 1;
            Vector2 velocity = body.getLinearVelocity();

            if (Math.abs(velocity.x) <= maxMoveVelocity) {
                body.applyLinearImpulse(impulse * moveAmount, 0, x, y, false);
            }
        }

    }
}
