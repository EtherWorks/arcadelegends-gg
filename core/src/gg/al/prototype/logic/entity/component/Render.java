package gg.al.prototype.logic.entity.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Thomas Neumann on 22.03.2017.
 */
public class Render extends PooledComponent {
    public AssetDescriptor<Texture> texture;
    public int width = 1;
    public int heigth = 1;
    public boolean transparent = true;
    public boolean rotate = true;

    @Override
    protected void reset() {
        texture = null;
        width = 1;
        heigth = 1;
        transparent = true;
        rotate = true;
    }
}
