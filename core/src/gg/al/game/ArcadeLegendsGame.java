package gg.al.game;

import com.badlogic.gdx.Game;
import gg.al.config.Config;
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
        config.editor.addConfigValueChangedListener(IVideoConfig.VideoKeyNames.VSYNC, (key, value) -> System.out.println(value));
        config.resetEditor();
        config.editor.setValue(IVideoConfig.VideoKeyNames.VSYNC, true);
        config.editor.flush();

        setScreen(new TestScreen(this));
    }


}
