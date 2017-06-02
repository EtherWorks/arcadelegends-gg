package gg.al.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.cyphercove.gdx.covetools.assets.AssignmentAssetManager;
import gg.al.config.*;
import gg.al.util.AudioManager;
import gg.al.util.ScreenManager;

/**
 * Created by Thomas Neumann on 18.03.2017.<p>
 * Service locator class for most classes in project
 */
public class AL extends Gdx {

    private static ArcadeLegendsGame game;

    private static Config config;
    private static ConfigEditor configEditor;
    private static IVideoConfig videoConfig;
    private static IAudioConfig audioConfig;
    private static IMiscellaneousConfig miscellaneousConfig;
    private static IGameplayConfig gameplayConfig;
    private static IInputConfig inputConfig;
    private static AssignmentAssetManager assetManager;
    private static ScreenManager screenManager;
    private static AudioManager audioManager;

    public static void provideGame(ArcadeLegendsGame game) {
        AL.game = game;
    }

    public static void provideConfig(Config config) {
        AL.config = config;
        AL.videoConfig = config.video;
        AL.audioConfig = config.audio;
        AL.miscellaneousConfig = config.miscellaneous;
        AL.gameplayConfig = config.gameplay;
        AL.inputConfig = config.input;
        AL.configEditor = config.editor;
    }

    public static void provideAssetManager(AssignmentAssetManager assetManager) {
        AL.assetManager = assetManager;
    }

    public static void provideScreenManager(ScreenManager screenManager) {
        AL.screenManager = screenManager;
    }

    public static void provideAudioManager(AudioManager audioManager) {
        AL.audioManager = audioManager;
    }

    public static ArcadeLegendsGame getGame() {
        return game;
    }

    public static Config getConfig() {
        return config;
    }

    public static ConfigEditor getConfigEditor() {
        return configEditor;
    }

    public static IVideoConfig getVideoConfig() {
        return videoConfig;
    }

    public static IAudioConfig getAudioConfig() {
        return audioConfig;
    }

    public static IMiscellaneousConfig getMiscellaneousConfig() {
        return miscellaneousConfig;
    }

    public static IGameplayConfig getGameplayConfig() {
        return gameplayConfig;
    }

    public static IInputConfig getInputConfig() {
        return inputConfig;
    }

    public static AssignmentAssetManager getAssetManager() {
        return assetManager;
    }

    public static ScreenManager getScreenManager() {
        return screenManager;
    }

    public static AudioManager getAudioManager() {
        return audioManager;
    }
}
