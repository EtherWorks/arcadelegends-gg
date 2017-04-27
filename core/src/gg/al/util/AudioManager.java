package gg.al.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.LongArray;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Thomas Neumann on 27.04.2017.
 */
public class AudioManager {

    private final ObjectMap<String, Sound> sounds;
    private final ObjectMap<Sound, LongArray> soundInstances;
    private final ObjectMap<String, Music> musics;

    private float masterVolume;
    private float musicVolume;
    private float effectVolume;

    public AudioManager(float masterVolume, float musicVolume, float effectVolume) {
        this.masterVolume = masterVolume;
        this.musicVolume = musicVolume;
        this.effectVolume = effectVolume;
        sounds = new ObjectMap<>();
        soundInstances = new ObjectMap<>();
        musics = new ObjectMap<>();
    }

    public void setEffectVolume(float effectVolume) {
        this.effectVolume = effectVolume;
        applyEffectVolume();
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
        applyEffectVolume();
        applyMusicVolume();
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
        applyMusicVolume();
    }

    private void applyMusicVolume() {
        float volume = musicVolume / 100 * masterVolume / 100;
        for (ObjectMap.Values<Music> values = musics.values();
             values.hasNext(); ) {
            Music music = values.next();
            music.setVolume(volume);
        }
    }

    private void applyEffectVolume() {
        float volume = effectVolume / 100 * masterVolume / 100;
        for (ObjectMap.Values<Sound> values = sounds.values();
             values.hasNext(); ) {
            Sound sound = values.next();
            for (long instance : soundInstances.get(sound).items) {
                sound.setVolume(instance, volume);
            }
        }
    }

    public void registerSound(String name, Sound sound) {
        sounds.put(name, sound);
        soundInstances.put(sound, new LongArray());
    }

    public void registerMusic(String name, Music music) {
        music.setVolume(musicVolume / 100 * masterVolume / 100);
        musics.put(name, music);
    }

    public void unregisterSound(String name) {
        Sound sound = sounds.get(name);
        soundInstances.remove(sound);
        sounds.remove(name);
    }

    public void unregisterMusic(String name) {
        musics.remove(name);
    }

    public long playSound(String name) {
        Sound sound = sounds.get(name);
        long instance = sound.play(effectVolume / 100 * masterVolume / 100);
        soundInstances.get(sound).add(instance);
        return instance;
    }

    public void playMusic(String name) {
        Music music = musics.get(name);
        music.play();
    }

    public void stopMusic(String name) {
        Music music = musics.get(name);
        music.stop();
    }
}
