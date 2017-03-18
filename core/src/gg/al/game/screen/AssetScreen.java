package gg.al.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;

import java.util.List;

/**
 * Created by Thomas Neumann on 18.03.2017.
 */
public interface AssetScreen extends Screen {
    List<AssetDescriptor> assets();
}
