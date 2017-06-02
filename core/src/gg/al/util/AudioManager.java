package gg.al.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Thomas Neumann on 27.04.2017.
 */
public class AudioManager {

    private final ObjectMap<String, Sound> sounds;
    private final ObjectMap<String, Music> musics;

    private AssetManager assetManager;

    private float masterVolume;
    private float musicVolume;
    private float effectVolume;

    public AudioManager(float masterVolume, float musicVolume, float effectVolume) {
        this.masterVolume = masterVolume;
        this.musicVolume = musicVolume;
        this.effectVolume = effectVolume;
        sounds = new ObjectMap<>();
        musics = new ObjectMap<>();
    }

    public void setEffectVolume(float effectVolume) {
        this.effectVolume = effectVolume;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
        applyMusicVolume();
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
        applyMusicVolume();
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    private void applyMusicVolume() {
        float volume = musicVolume / 100 * masterVolume / 100;
        for (ObjectMap.Values<Music> values = musics.values();
             values.hasNext(); ) {
            Music music = values.next();
            music.setVolume(volume);
        }
    }

    public void registerSound(String name, Sound sound) {
        sounds.put(name, sound);
    }

    public void registerMusic(String name, Music music) {
        music.setVolume(musicVolume / 100 * masterVolume / 100);
        musics.put(name, music);
    }

    public void unregisterSound(String name) {
        Sound sound = sounds.get(name);
        sounds.remove(name);
    }

    public void unregisterMusic(String name) {
        musics.remove(name);
    }

    public long playSound(String name) {
        return sounds.get(name).play(effectVolume / 100 * masterVolume / 100);
    }

    public void playMusic(String name) {
        musics.get(name).play();
    }

    public void stopMusic(String name) {
        musics.get(name).stop();
    }

    public void pauseMusic(String name) {
        musics.get(name).pause();
    }

    public void setMusicLooping(String name, boolean looping) {
        musics.get(name).setLooping(looping);
    }

}
