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
    /**
     * Returns a {@link Screen} which loads the denoted {@link gg.al.util.Assets} of the {@link IAssetScreen}.
     *
     * @param screen the {@link IAssetScreen} from which the assets should be loaded
     * @return the {@link ILoadingScreen}
     */
    Screen withAssetScreen(IAssetScreen screen);
}
