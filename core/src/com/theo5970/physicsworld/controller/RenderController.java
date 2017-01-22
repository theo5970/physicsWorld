package com.theo5970.physicsworld.controller;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;
import com.theo5970.physicsworld.GameObject;
import com.theo5970.physicsworld.component.*;
import com.theo5970.physicsworld.manager.ObjectManager;
import com.theo5970.physicsworld.manager.ParticleManager;

import java.util.*;

public class RenderController {
    private static class ZComparator implements Comparator<GameObject> {
        @Override
        public int compare (GameObject o1, GameObject o2) {
            float z1 = o1.getComponent(TransformComponent.class).position.z;
            float z2 = o2.getComponent(TransformComponent.class).position.z;

            return (z1 - z2) > 0 ? 1 : ((z1 == z2) ? 0 : -1);
        }
    }

    private static final ZComparator comparator = new ZComparator();
    private static final List<GameObject> objects = new ArrayList<GameObject>();
    private static final List<ParticleEffectPool.PooledEffect> pooledEffects = new ArrayList<ParticleEffectPool.PooledEffect>();

    public static void addObject(GameObject object) {
        objects.add(object);
    }

    public static void render(SpriteBatch batch) {

        Collections.sort(objects, comparator);

        for (GameObject object : objects) {
            TextureComponent textureComponent = object.getComponent(TextureComponent.class);
            TransformComponent transform = object.getComponent(TransformComponent.class);
            ColorComponent colorComponent = object.getComponent(ColorComponent.class);

            Texture texture = textureComponent.texture;
            float width = transform.size.x, height = transform.size.y;
            float originX = width / 2, originY = height / 2;
            float x = transform.position.x - originX, y = transform.position.y - originY;
            float scaleX = transform.scale.x, scaleY = transform.scale.y;
            float rotation = transform.degrees;

            int srcWidth = texture.getWidth(), srcHeight = texture.getHeight();

            TextureRotation textureRotation = textureComponent.textureRotation;

            boolean flipX = textureRotation == TextureRotation.X || textureRotation == TextureRotation.XY;
            boolean flipY = textureRotation == TextureRotation.Y || textureRotation == TextureRotation.XY;

            // draw(Texture texture, float x, float y, float originX, float originY, float width, float height,
            // float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight,
            // boolean flipX, boolean flipY)

            if (object.hasComponent(ParticleComponent.class)) {
                ParticleComponent particleComponent = object.getComponent(ParticleComponent.class);
                if (particleComponent.enabled) {
                    ParticleEffectPool effectPool = particleComponent.pool;
                    ParticleEffectPool.PooledEffect pooledEffect = effectPool.obtain();
                    pooledEffect.setPosition(x + originX, y + originY);
                    if (particleComponent.isOnce) {
                        if (!pooledEffects.contains(pooledEffect)) {
                            pooledEffects.add(pooledEffect);
                        }
                    } else {
                        pooledEffects.add(pooledEffect);
                    }
                }
            }
            batch.setColor(colorComponent.color);
            batch.draw(texture, x, y, originX, originY, width, height, scaleX, scaleY,
                    rotation, 0, 0, srcWidth, srcHeight,
                    flipX, flipY);
        }
        for (int i=0; i<pooledEffects.size(); i++) {
            ParticleEffectPool.PooledEffect pooledEffect = pooledEffects.get(i);
            pooledEffect.update(Gdx.graphics.getDeltaTime());
            pooledEffect.draw(batch);
            if (pooledEffect.isComplete()) {
                pooledEffect.free();
                pooledEffects.remove(i);
            }
        }
        objects.clear();
    }
}

