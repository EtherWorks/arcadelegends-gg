package gg.al.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import gg.al.config.Config;
import gg.al.config.IVideoConfig;
import gg.al.game.screen.TestScreen;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 11.03.2017.
 */
@Slf4j
public class ArcadeLegendsGame extends Game {
    public final Config config;

    public ArcadeLegendsGame(Config config) {
        this.config = config;
    }

    @Override
    public void create() {
        config.editor.addConfigValueChangedListener((key, value) -> log.debug("Config value changed: {}={}", key, value));
        config.editor.addConfigValueChangedListener(IVideoConfig.VideoKeyNames.FULLSCREEN, (key, value) -> {
            boolean fullscreen = (boolean) value;
            if (fullscreen)
                Gdx.app.postRunnable(() -> Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()));
            else
                Gdx.app.postRunnable(() -> Gdx.graphics.setWindowedMode(960, 540));
        });
        setScreen(new TestScreen(this));
    }


}
