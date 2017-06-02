package gg.al.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;

import java.util.List;

/**
 * Created by Thomas Neumann on 18.03.2017.<br />
 * Interface extending {@link Screen}, which adds methods for returning
 * the needed assets for this screen, as well as an {@link ILoadingScreen}
 * to use to display while loading.
 */
public interface IAssetScreen extends Screen {
    Object assets();
    ILoadingScreen customLoadingScreen();
}
