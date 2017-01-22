package com.theo5970.physicsworld.component;

import com.badlogic.gdx.math.Vector2;

public class SpringComponent implements Component {
    public Vector2 springVelocity;
    public SpringComponent(Vector2 springVelocity) {
        this.springVelocity = springVelocity;
    }
}
