package gg.al.logic.entity.component;

import com.artemis.Component;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Thomas Neumann on 22.03.2017.
 */
public class Render extends Component {
    public AssetDescriptor<Texture> texture;
    public int width = 1;
    public int heigth = 1;
    public boolean transparent = true;
    public boolean rotate = true;
}
