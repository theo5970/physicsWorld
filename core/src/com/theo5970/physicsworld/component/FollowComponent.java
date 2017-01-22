package com.theo5970.physicsworld.component;

import com.theo5970.physicsworld.GameObject;

public class FollowComponent implements Component {
    public GameObject following;

    public FollowComponent(GameObject followingObject) {
        following = followingObject;
    }
}
