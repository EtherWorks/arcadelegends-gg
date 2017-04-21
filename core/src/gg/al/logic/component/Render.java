package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Render extends PooledComponent {

    public float width;
    public float height;
    public boolean transparent;
    public AssetDescriptor<Texture> texture;

    @Override
    protected void reset() {
        width = 0;
        height = 0;
        transparent = false;
        texture = null;
    }

    public void set(float width, float height, boolean transparent, AssetDescriptor<Texture> texture)
    {
        this.width = width;
        this.height = height;
        this.transparent = transparent;
        this.texture = texture;
    }

}
