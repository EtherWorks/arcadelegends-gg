package gg.al.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import gg.al.config.Config;
import gg.al.game.screen.MainMenuScreen;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 11.03.2017.
 */
@Slf4j
public class ArcadeLegendsGame extends Game {
    @Getter
    private final Config config;

    private final AssetManager assetManager;

    public ArcadeLegendsGame(Config config) {
        this.config = config;
        this.assetManager = new AssetManager();
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

        setScreen(new MainMenuScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }
}
