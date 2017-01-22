package com.theo5970.physicsworld.manager;

import com.theo5970.physicsworld.GameObject;
import com.theo5970.physicsworld.component.ParticleComponent;
import com.theo5970.physicsworld.controller.RenderController;

import java.util.ArrayList;
import java.util.List;

public class ObjectManager {
    private static ArrayList<GameObject> gameObjectList;
    private static ArrayList<Long> beRemovedObjectList;
    static {
        gameObjectList = new ArrayList<GameObject>();
        beRemovedObjectList = new ArrayList<Long>();
    }

    public static void register(GameObject object) {
        gameObjectList.add(object);
    }

    public static void addBeRemoved(GameObject object) {
        beRemovedObjectList.add(object.getId());
    }
    public static void updateBeRemoved() {
        if (beRemovedObjectList.size() == 0) return;

        for (int i=0; i<gameObjectList.size(); i++) {
            GameObject object = gameObjectList.get(i);
            if (beRemovedObjectList.contains(object.getId())) {
                object.dispose();
                GameObject.minusCount();
                gameObjectList.remove(i);
            }
        }
        beRemovedObjectList.clear();
    }

    public static ArrayList<GameObject> getList() {
        return gameObjectList;
    }
}
