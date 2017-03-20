package gg.al.game.screen;

import com.badlogic.gdx.Screen;

/**
 * Created by Thomas on 20.03.2017.<br />
 * Interface extending {@link Screen}, which adds one method for
 * configuring the LoadingScreen. It is the responsibility of the
 * LoadingScreen to load all required assets before switching to
 * the new {@link IAssetScreen}.
 */
public interface ILoadingScreen extends Screen{
    Screen withAssetScreen(IAssetScreen screen);
}
