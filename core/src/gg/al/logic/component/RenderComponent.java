package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class RenderComponent extends PooledComponent implements ITemplateable {

    public final ObjectMap<RenderState, AssetDescriptor<Texture>> textures;
    public final ObjectMap<RenderState, Animation<TextureRegion>> animations;
    public RenderTemplate renderTemplate;
    public float width;
    public float height;
    public boolean transparent;
    public boolean flipX;
    public boolean flipY;
    public RenderState renderState;

    public RenderComponent() {
        textures = new ObjectMap<>();
        animations = new ObjectMap<>();
    }

    public boolean facingRight() {
        return !flipX;
    }

    public boolean facingLeft() {
        return flipX;
    }

    @Override
    protected void reset() {
        width = 0;
        height = 0;
        transparent = false;
        animations.clear();
        textures.clear();
        renderState = RenderState.IDLE;
        renderTemplate = null;
        flipX = false;
        flipY = false;
    }

    @Override
    public Template getTemplate() {
        return new RenderTemplate();
    }

    @Override
    public void fromTemplate(Template template) {
        RenderTemplate renderTemplate = (RenderTemplate) template;
        width = renderTemplate.width;
        height = renderTemplate.height;
        transparent = renderTemplate.transparent;
        renderState = RenderState.IDLE;
        flipX = false;
        flipY = false;
        this.renderTemplate = renderTemplate;
    }

    public enum RenderState {
        IDLE, MOVE_UP, MOVE_SIDE, MOVE_DOWN, ATTACK, TAUNT
    }

    public static class RenderTemplate extends Template {
        public float width;
        public float height;
        public boolean transparent;
        public Map<RenderState, AnimationTemplate> animationTemplates = new HashMap();
        {
            for (RenderState state : RenderState.values())
            {
                animationTemplates.put(state, new AnimationTemplate());
            }
        }

        public static class AnimationTemplate {
            public int frameColumns;
            public int frameRows;
            public float frameDuration;
            public String texture = "";
        }
    }
}
