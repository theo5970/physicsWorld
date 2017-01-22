package com.theo5970.physicsworld.builder.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class ShapeBuilder {

    public static PolygonShape buildAsBox (float width, float height) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        return shape;
    }

    public static PolygonShape buildAsBox (float width, float height, Vector2 center) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2, center, 0);
        return shape;
    }

    public static CircleShape buildCircle (float radius) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        return circleShape;
    }

    public static PolygonShape buildIncline(float width, float height, int rotation) {
        float x = -width / 2;
        float y = -height / 2;
        float[] vertices = new float[6];
        switch (rotation) {
            case 0:
                vertices = new float[]{x, y, x + width, y, x + width, y + height};
                break;
            case 1:
                vertices = new float[]{x, y + height, x, y, x + width, y};
                break;
            case 2:
                vertices = new float[]{x + width, y + height, x, y, x + width, y + height};
                break;
            case 3:
                vertices = new float[]{x + width, y + height, x + width, y, x + width, y + height};
        }
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        return shape;
    }
}
