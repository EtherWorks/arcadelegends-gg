package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.graphics.renderer.BulletRenderer;
import gg.al.graphics.renderer.CharacterRenderer;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.Template;
import gg.al.logic.system.RenderSystem;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
@Slf4j
public class RenderComponent extends PooledComponent implements ITemplateable {

    public static final CharacterRenderer CHARACTER_RENDERER = new CharacterRenderer();
    public static final BulletRenderer BULLET_RENDERER = new BulletRenderer();
    public static final RenderDelegate NULL_RENDERER = new RenderDelegate() {
        @Override
        public void inserted(int entity, RenderSystem renderSystem) {
            log.debug("Entity: {} NULL_RENDERER > inserted", entity);
        }

        @Override
        public void process(int entity, RenderSystem renderSystem) {

        }

        @Override
        public void removed(int entity, RenderSystem renderSystem) {
            log.debug("Entity: {} NULL_RENDERER > removed", entity);
        }

        @Override
        public String defaultRenderState() {
            return null;
        }
    };
    @Getter(lazy = true)
    private final static TextureRegion NULL_FRAME = new TextureRegion(new Texture(new Pixmap(1, 1, Pixmap.Format.RGBA8888)));
    public final ObjectMap<String, Animation<TextureRegion>> animations;
    public final ObjectMap<String, Float> stateTimes;
    public RenderTemplate renderTemplate;
    public float width;
    public float height;
    public boolean transparent;
    public boolean flipX;
    public boolean flipY;
    public String renderState;
    public RenderDelegate renderDelegate;

    public RenderComponent() {
        animations = new ObjectMap<>();
        stateTimes = new ObjectMap<>();
        renderState = "";
    }

    public boolean facingRight() {
        return !flipX;
    }

    public boolean facingLeft() {
        return flipX;
    }

    public void faceLeft() {
        flipX = true;
    }

    public void faceRight() {
        flipX = false;
    }

    public boolean isInState(String state) {
        return renderState.equals(state);
    }

    public boolean isInAnyState(String... states) {
        for (int i = 0; i < states.length; i++) {
            if (isInState(states[i]))
                return true;
        }
        return false;
    }

    public boolean isInAnyState(Enum... states) {
        for (int i = 0; i < states.length; i++) {
            if (isInState(states[i]))
                return true;
        }
        return false;
    }

    public void setRenderState(Enum state) {
        renderState = state.name();
    }

    public boolean isInState(Enum state) {
        return isInState(state.name());
    }

    public TextureRegion getCurrentKeyFrame(float stateTime) {
        Animation<TextureRegion> anim = animations.get(renderState);
        return anim == null ? getNULL_FRAME() : anim.getKeyFrame(stateTime);
    }

    public RenderTemplate.AnimationTemplate getCurrentAnimation()
    {
        return renderTemplate.animationTemplates.get(renderState);
    }

    @Override
    protected void reset() {
        width = 0;
        height = 0;
        transparent = false;
        animations.clear();
        renderState = "";
        renderTemplate = null;
        renderDelegate = null;
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
        flipX = false;
        flipY = false;
        this.renderTemplate = renderTemplate;
        switch (renderTemplate.renderType) {
            case "PLAYER":
                this.renderDelegate = CHARACTER_RENDERER;
                break;
            case "BULLET":
                this.renderDelegate = BULLET_RENDERER;
                break;
            default:
                renderDelegate = NULL_RENDERER;
                break;
        }
        this.renderState = renderDelegate.defaultRenderState();
    }

    public interface RenderDelegate {
        void inserted(int entity, RenderSystem renderSystem);

        void process(int entity, RenderSystem renderSystem);

        void removed(int entity, RenderSystem renderSystem);

        String defaultRenderState();
    }

    public static class RenderTemplate extends Template {
        public float width;
        public float height;
        public boolean transparent;
        public Map<String, AnimationTemplate> animationTemplates = new HashMap();
        public String renderType = "";

        @Override
        public String toString() {
            return "RenderTemplate{" +
                    "width=" + width +
                    ", height=" + height +
                    ", transparent=" + transparent +
                    ", animationTemplates=" + animationTemplates +
                    '}';
        }

        public static class AnimationTemplate {
            public int frameColumns;
            public int frameRows;
            public float frameDuration;
            public String texture = "";
            public float width = 1;
            public float height = 1;
            public Animation.PlayMode playMode = Animation.PlayMode.LOOP;
        }
    }
}
