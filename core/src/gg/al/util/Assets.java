package gg.al.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Thomas Neumann on 18.03.2017.
 */
public interface Assets {
    AssetDescriptor<Skin> TEXTBUTTONSTYLES = new AssetDescriptor<Skin>("assets/prototype/styles/buttonfont/textbuttonstyles.json", Skin.class);
}
