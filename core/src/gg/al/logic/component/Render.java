package gg.al.logic.component;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import gg.al.logic.entity.EntityArguments;
import gg.al.util.Assets;

import java.util.Map;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Render extends PooledDefComponent {

    public float width;
    public float height;
    public boolean transparent;
    public String textureName;

    public int frameColumns;
    public int frameRows;
    public float frameDuration;

    public AssetDescriptor<Texture> texture;
    public Animation<TextureRegion> animation;

    @Override
    protected void reset() {
        width = 0;
        height = 0;
        frameColumns = 0;
        frameRows = 0;
        transparent = false;
        textureName = null;
        texture = null;
        animation = null;
    }

    @Override
    public IComponentDef getDefaultDef() {
        return new RenderDef();
    }

    @Override
    public void fromDef(IComponentDef def) {
        RenderDef renderDef = (RenderDef) def;
        width = renderDef.width;
        height = renderDef.height;
        transparent = renderDef.transparent;
        textureName = renderDef.texture;
        frameRows = renderDef.frameRows;
        frameColumns = renderDef.frameColumns;
        frameDuration = renderDef.frameDuration;
    }

    public static class RenderDef extends IComponentDef
    {
        public float width;
        public float height;
        public boolean transparent;
        public String texture = "";
        public int frameColumns;
        public int frameRows;
        public float frameDuration;
    }
}
