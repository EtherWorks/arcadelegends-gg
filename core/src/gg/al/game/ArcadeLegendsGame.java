package gg.al.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import gg.al.config.Config;
import gg.al.game.screen.AssetScreen;
import gg.al.game.screen.LoadingScreen;
import gg.al.game.screen.MainMenuScreen;
import gg.al.util.ScreenManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 11.03.2017.
 */
@Slf4j
public class ArcadeLegendsGame extends Game {
    @Getter
    private final Config config;

    @Getter
    private final AssetManager assetManager;

    @Getter
    private final ScreenManager screenManager;

    public ArcadeLegendsGame(Config config) {
        this.config = config;
        this.assetManager = new AssetManager();
        this.screenManager = new ScreenManager();
    }

    @Override
    public void create() {
        AL.game = this;
        AL.asset = assetManager;
        AL.config = config;
        AL.cedit = config.editor;
        AL.caudio = config.audio;
        AL.cgameplay = config.gameplay;
        AL.cinput = config.input;
        AL.cmisc = config.miscellaneous;
        AL.cvideo = config.video;
        AL.screen = screenManager;
        AL.screen.register(new LoadingScreen(), LoadingScreen.class);

        AL.screen.register(new MainMenuScreen(), MainMenuScreen.class);
        setScreen(AL.screen.get(MainMenuScreen.class));
    }

    @Override
    public void dispose() {
        super.dispose();
        screenManager.dispose();
        assetManager.dispose();
    }

    public void setScreen(AssetScreen screen) {
        this.setScreen(AL.screen.get(LoadingScreen.class).withAssetScreen(screen));
    }
}
