package gg.al.game.screen;

import com.badlogic.gdx.Screen;

/**
 * Created by Thomas on 20.03.2017.
 */
public interface ILoadingScreen extends Screen{
    Screen withAssetScreen(IAssetScreen screen);
}
