package gg.al.logic.component;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import gg.al.logic.entity.EntityArguments;

import java.util.Map;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Render extends PooledDefComponent {

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

    public void set(EntityArguments arguments)
    {
        Map<String, Object> render = arguments.get("Render", Map.class);
        this.height = (float)(double)render.get("height");
        this.width = (float)(double)render.get("width");
        this.transparent = (boolean)render.get("transparent");
        render.get("texture");
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
    }

    public static class RenderDef extends IComponentDef
    {
        public float width;
        public float height;
        public boolean transparent;
        public String texture = "";
    }
}
