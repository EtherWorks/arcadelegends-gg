package gg.al.game.screen;

import com.badlogic.gdx.Screen;
import gg.al.game.ArcadeLegendsGame;

/**
 * Created by Thomas Neumann on 16.03.2017.
 */
public abstract class ArcadeScreen implements Screen {
    protected final ArcadeLegendsGame game;

    public ArcadeScreen(ArcadeLegendsGame game) {
        this.game = game;
    }
}
