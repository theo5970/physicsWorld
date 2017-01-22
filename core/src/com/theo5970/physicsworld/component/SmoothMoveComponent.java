package com.theo5970.physicsworld.component;

import com.badlogic.gdx.math.MathUtils;

public class SmoothMoveComponent implements Component {
    public float amount = 1;
    public float pos;
    public boolean reversed;
    public SmoothMoveComponent(float amount) {

        if (amount >= 1.0f) {
            this.amount = amount;
            this.pos = MathUtils.random(amount);
        }
    }
}
