package com.theo5970.physicsworld.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.theo5970.physicsworld.component.BodyComponent;
import com.theo5970.physicsworld.controller.PlayerController;

public class CameraManager {
    // 카메라
    private static OrthographicCamera camera;
    // 월드와 화면의 비율
    private static float worldRatio;
    // 최대, 최소 확대비율
    private static float maxZoom = 1.6f;
    private static float minZoom = 0.8f;
    // 월드의 가로, 세로 너비
    private static float width;
    private static float height;
    // 현재 확대비율
    private static float zoom = 1.0f;
    public static void init(float ratio) {
        worldRatio = ratio;
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / ratio,
                Gdx.graphics.getHeight() / ratio);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        width = camera.viewportWidth;
        height = camera.viewportHeight;
        camera.update();
    }

    public static float getWorldRatio() {
        return worldRatio;
    }
    public static void reinit() {
        camera.setToOrtho(false, Gdx.graphics.getWidth() / worldRatio, Gdx.graphics.getHeight() / worldRatio);
        width = camera.viewportWidth;
        height = camera.viewportHeight;
        camera.update();
    }
    // 카메라의 행렬 반환
    public static Matrix4 getCombined() {
        return camera.combined;
    }
    // Unproject
    public static Vector3 unproject(Vector2 pos) {
        return camera.unproject(new Vector3(pos, 0));
    }
    // Zoom-In (확대)
    public static void zoomIn() {
        zoom -= 0.02f;
        // 최소 비율 이하이면 고정하기
        if (zoom <= minZoom) {
            zoom = minZoom;
        }
    }
    // Zoom-Out (축소)
    public static void zoomOut() {
        zoom += 0.02f;
        // 최대 비율 이상이면 고정하기
        if (zoom >= maxZoom) {
            zoom = maxZoom;
        }
    }
    // 카메라 회전각도 가져오기
    private static float getCameraRotation()
    {
        float camAngle = -MathUtils.atan2(camera.up.x, camera.up.y)* MathUtils.radiansToDegrees + 180;
        return camAngle;
    }
    // 자동으로 알아서 플레이어와 맞추기
    public static void updateAutomatic() {
        Body body = PlayerController.getPlayerObject()
                .getComponent(BodyComponent.class).getBody();
        Vector2 player_pos = body.getPosition();

        float targetX = camera.viewportWidth / 2;
        float targetY = camera.viewportHeight / 2;

        if (player_pos.x > width / 2) {
            targetX = player_pos.x;
        }
        if (player_pos.y > height / 2) {
            targetY = player_pos.y;
        }

        targetX += (camera.zoom - 1) * (camera.viewportWidth / 2);
        targetY += (camera.zoom - 1) * (camera.viewportHeight / 2);

        camera.position.x += (targetX - camera.position.x) * 0.1f;
        camera.position.y += (targetY - camera.position.y) * 0.1f;
        camera.zoom += (zoom - camera.zoom) * 0.03f;

        camera.update();
    }
    public static Vector3 getCameraPosition() {
        return camera.position;
    }
    public static float getViewportWidth() {
        return camera.viewportWidth;
    }
    public static float getViewportHeight() {
        return camera.viewportHeight;
    }
}
