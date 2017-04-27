package gg.al.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.github.drapostolos.typeparser.TypeParser;
import gg.al.config.Config;
import gg.al.config.IAudioConfig;
import gg.al.game.screen.DefaultLoadingScreen;
import gg.al.game.screen.IAssetScreen;
import gg.al.game.screen.MainMenuScreen;
import gg.al.util.AudioManager;
import gg.al.util.ScreenManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 11.03.2017.<p>
 * Main Game class for the ArcadeLegends game.
 */
@Slf4j
public class ArcadeLegendsGame extends Game {

    @Getter
    private final Config config;

    @Getter
    private final AssetManager assetManager;

    @Getter
    private final ScreenManager screenManager;

    private final AudioManager audioManager;

    public ArcadeLegendsGame(Config config) {
        this.config = config;
        this.assetManager = new AssetManager();
        this.screenManager = new ScreenManager();
        this.audioManager = new AudioManager(config.audio.masterVolume(), config.audio.musicVolume(), config.audio.effectVolume());
    }

    @Override
    public void create() {
        AL.game = this;
        AL.asset = assetManager;
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));

        AL.config = config;
        AL.cedit = config.editor;
        AL.caudio = config.audio;
        AL.cgameplay = config.gameplay;
        AL.cinput = config.input;
        AL.cmisc = config.miscellaneous;
        AL.cvideo = config.video;
        AL.screen = screenManager;
        AL.audioManager = audioManager;

        TypeParser parser = TypeParser.newBuilder().build();
        AL.cedit.addConfigValueChangedListener(IAudioConfig.AudioKeys.masterVolume, (key, value) -> {
            float masterVolume = parser.parse(value, Float.class);
            audioManager.setMasterVolume(masterVolume);
        });
        AL.cedit.addConfigValueChangedListener(IAudioConfig.AudioKeys.effectVolume, (key, value) -> {
            float effectVolume = parser.parse(value, Float.class);
            audioManager.setEffectVolume(effectVolume);
        });
        AL.cedit.addConfigValueChangedListener(IAudioConfig.AudioKeys.musicVolume, (key, value) -> {
            float musicVolume = parser.parse(value, Float.class);
            audioManager.setMusicVolume(musicVolume);
        });

        setScreen(AL.screen.get(MainMenuScreen.class, true));
    }

    @Override
    public void dispose() {
        super.dispose();
        screenManager.dispose();
        assetManager.dispose();
    }

    public void setScreen(IAssetScreen screen) {
        if (screen.customLoadingScreen() != null)
            this.setScreen(screen.customLoadingScreen().withAssetScreen(screen));
        else
            this.setScreen(AL.screen.get(DefaultLoadingScreen.class, true).withAssetScreen(screen));
    }
}
