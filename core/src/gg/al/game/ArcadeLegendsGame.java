package gg.al.game;

import com.badlogic.gdx.Game;
import gg.al.config.Config;
import gg.al.config.ConfigEditor;
import gg.al.config.IVideoConfig;
import gg.al.game.screen.TestScreen;

/**
 * Created by Thomas Neumann on 11.03.2017.
 */
public class ArcadeLegendsGame extends Game {
    public final Config config;

    public ArcadeLegendsGame(Config config) {
        this.config = config;
    }

    @Override
    public void create() {
        ConfigEditor edit = config.buildEditor();
        edit.setValue(IVideoConfig.VideoKeyNames.VSYNC, true);
        edit.flush();
        setScreen(new TestScreen(this));
    }


}
