package com.theo5970.physicsworld.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public class SoundManager {
    private static ObjectMap<String, Sound> soundMap;
    static {
        soundMap = new ObjectMap<String, Sound>();
    }
    public static void register(String key, String filename) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filename));
        soundMap.put(key, sound);
    }

    public static void play(String key) {
        soundMap.get(key).play();
    }

    public static void play(String key, float volume) {
        soundMap.get(key).play(volume);
    }

    public static void play(String key, float volume, float pitch) {
        soundMap.get(key).play(volume, pitch, 0);
    }
}
