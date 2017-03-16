package gg.al.game;

import com.badlogic.gdx.Game;
import gg.al.config.Config;
import gg.al.game.screen.TestScreen;
import gg.al.game.screen.UITestScreen;
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
        setScreen(new UITestScreen(this));
    }


}
