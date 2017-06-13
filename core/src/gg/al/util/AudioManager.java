package gg.al.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Thomas Neumann on 27.04.2017.<br>
 * {@link AudioManager} class, used for registering {@link Sound} and {@link Music} and controlling the volume of registered sounds and music.
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

    /**
     * Sets the volume of the sounds in percent.
     *
     * @param effectVolume the volume to be set
     */
    public void setEffectVolume(float effectVolume) {
        this.effectVolume = effectVolume;
    }

    /**
     * Sets the master volume.
     *
     * @param masterVolume the volume to be set
     */
    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
        applyMusicVolume();
    }

    /**
     * Sets the volume of the musics in percent.
     *
     * @param musicVolume the volume to be set
     */
    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
        applyMusicVolume();
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Recalculates volume and applies it to registered instances of {@link Music} and {@link Sound}
     */
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

    /**
     * Plays the {@link Sound} with the given name.
     *
     * @param name the {@link Sound} to be played
     * @return the sound handle
     */
    public long playSound(String name) {
        return sounds.get(name).play(effectVolume / 100 * masterVolume / 100);
    }

    /**
     * Plays the {@link Music} with the given name.
     *
     * @param name the {@link Music} to be played
     */
    public void playMusic(String name) {
        musics.get(name).play();
    }

    /**
     * Stops the {@link Music} with the given name.
     *
     * @param name the {@link Music} to be stopped
     */
    public void stopMusic(String name) {
        musics.get(name).stop();
    }

    /**
     * Pauses the {@link Music} with the given name.
     *
     * @param name the {@link Music} to be paused
     */
    public void pauseMusic(String name) {
        musics.get(name).pause();
    }

    /**
     * Sets the looping flag on the given {@link Music}
     *
     * @param name    the name of the {@link Music}
     * @param looping if the music should loop
     */
    public void setMusicLooping(String name, boolean looping) {
        musics.get(name).setLooping(looping);
    }

}
