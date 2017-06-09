package gg.al.graphics.renderer;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.game.AL;
import gg.al.logic.component.*;
import gg.al.logic.system.RenderSystem;
import gg.al.util.Assets;

import java.util.Map;

import static gg.al.graphics.renderer.CharacterRenderer.PlayerRenderState.*;

/**
 * Created by Thomas Neumann on 29.05.2017.<br />
 */
public class CharacterRenderer implements RenderComponent.RenderDelegate {

    private final static int RESOURCE_OFFSET = 45;
    private final static int UI_WIDTH = 250;
    private final static int UI_HEIGHT = 200;
    private ObjectMap<Integer, FrameBuffer> healthBuffers;
    private ObjectMap<Integer, FrameBuffer> resourceBuffers;
    private ObjectMap<Integer, TextureRegion> healthBars;
    private ObjectMap<Integer, TextureRegion> resourceBars;
    private Camera cameraBar;
    private Camera uiCamera;

    private final Color healthBarColor = new Color(0.7f, 0, 0, 1);
    private final Color resourceBarColor = new Color(0,0,0.7f,1);

    public CharacterRenderer() {
        healthBars = new ObjectMap<>();
        resourceBars = new ObjectMap<>();
        resourceBuffers = new ObjectMap<>();
        healthBuffers = new ObjectMap<>();
    }

    private static void drawToBuffer(FrameBuffer buffer, Color color, Texture gradient, float perc, SpriteBatch shaderBatch, ShaderProgram shader, Matrix4 projection) {
        buffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        shaderBatch.setProjectionMatrix(projection);
        shaderBatch.begin();
        shaderBatch.setColor(color);
        shader.setUniformf("u_gradient", perc);
        shaderBatch.draw(gradient, -buffer.getWidth() / 2, -buffer.getHeight() / 2);
        shaderBatch.end();
        buffer.end();
    }

    @Override
    public void inserted(int entityId, RenderSystem renderSystem) {
        if (cameraBar == null) {
            cameraBar = new OrthographicCamera(renderSystem.getLevelAssets().health_bar_gradient.getWidth(),
                    renderSystem.getLevelAssets().health_bar_gradient.getHeight());
            uiCamera = new OrthographicCamera(UI_WIDTH, UI_HEIGHT);
        }

        RenderComponent render = renderSystem.getMapperRender().get(entityId);
        PositionComponent position = renderSystem.getMapperPosition().get(entityId);
        if (render.animations.size == 0) {
            for (Map.Entry<String, RenderComponent.RenderTemplate.AnimationTemplate> entry : render.renderTemplate.animationTemplates.entrySet()) {
                TextureRegion texture = renderSystem.getLevelAssets().get(entry.getValue().texture);

                TextureRegion[][] tmp = texture.split(
                        +texture.getRegionWidth() / entry.getValue().frameColumns,
                        +texture.getRegionHeight() / entry.getValue().frameRows);
                TextureRegion[] frames = new TextureRegion[entry.getValue().frameColumns * entry.getValue().frameRows];
                int index = 0;
                for (int i = 0; i < entry.getValue().frameRows; i++) {
                    for (int j = 0; j < entry.getValue().frameColumns; j++) {
                        frames[index++] = tmp[i][j];
                    }
                }
                render.animations.put(entry.getKey(), new Animation<>(entry.getValue().frameDuration, new Array<>(frames), entry.getValue().playMode));
                render.stateTimes.put(entry.getKey(), 0f);
            }
        }
        Decal decal = Decal.newDecal(render.width, render.height, render.getCurrentKeyFrame(renderSystem.getStateTime()), render.transparent);
        decal.rotateX(renderSystem.getWorldDegree());
        decal.setPosition(position.position.x, position.position.y, 0);
        renderSystem.getDecalMap().put(entityId, decal);

        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, UI_WIDTH, UI_HEIGHT, false);
        Assets.LevelAssets levelAssets = renderSystem.getLevelAssets();
        FrameBuffer healthBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, levelAssets.health_bar_gradient.getWidth(), levelAssets.health_bar_gradient.getHeight(), false);
        TextureRegion healthBar = new TextureRegion(healthBuffer.getColorBufferTexture());
        healthBar.flip(false, true);
        FrameBuffer resourceBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, levelAssets.health_bar_gradient.getWidth(), levelAssets.health_bar_gradient.getHeight(), false);
        TextureRegion resourceBar = new TextureRegion(resourceBuffer.getColorBufferTexture());
        resourceBar.flip(false, true);

        healthBars.put(entityId, healthBar);
        resourceBars.put(entityId, resourceBar);

        healthBuffers.put(entityId, healthBuffer);
        resourceBuffers.put(entityId, resourceBuffer);

        buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        renderSystem.getBuffers().put(entityId, buffer);

        TextureRegion uiTextureRegion = new TextureRegion(buffer.getColorBufferTexture());
        uiTextureRegion.flip(false, true);
        Decal uiDecal = Decal.newDecal(1.8f, 1f, uiTextureRegion, true);
        uiDecal.setPosition(position.position.x, position.position.y, 1);
        renderSystem.getUiMap().put(entityId, uiDecal);
    }

    @Override
    public void process(int entityId, RenderSystem renderSystem) {
        PhysicComponent physic = renderSystem.getMapperPhysic().get(entityId);
        RenderComponent render = renderSystem.getMapperRender().get(entityId);
        StatComponent stats = renderSystem.getMapperStats().get(entityId);

        CharacterComponent character = renderSystem.getMapperCharacterComponent().get(entityId);
        Decal decal = renderSystem.getDecalMap().get(entityId);
        float stateTime = renderSystem.getStateTime();
        if (character != null && render.isInState(ATTACK)) {
            stateTime = render.animations.get(PlayerRenderState.ATTACK.name()).getAnimationDuration() * character.getRenderMultiplicator();
        } else if (character != null && render.isInAnyState(
                ABILITY_1, ABILITY_2, ABILITY_3, ABILITY_4, TRAIT
        )) {
            stateTime = render.stateTimes.get(render.renderState);
            stateTime += renderSystem.getDelta();
            if (render.animations.get(render.renderState).isAnimationFinished(stateTime)) {
                render.stateTimes.put(render.renderState, 0f);
                render.setRenderState(IDLE);
            } else
                render.stateTimes.put(render.renderState, stateTime);
        }
        TextureRegion region = render.getCurrentKeyFrame(stateTime);
        region.flip(region.isFlipX() ^ render.flipX, region.isFlipY() ^ render.flipY);
        decal.setTextureRegion(region);
        RenderComponent.RenderTemplate.AnimationTemplate template = render.getCurrentAnimation();
        decal.setWidth(template.width);
        decal.setHeight(template.height);

        decal.setPosition(physic.body.getPosition().x, physic.body.getPosition().y, decal.getZ());

        SpriteBatch shaderBatch = renderSystem.getShaderBatch();
        ShaderProgram shader = renderSystem.getGradientShader();
        drawToBuffer(healthBuffers.get(entityId), healthBarColor, renderSystem.getLevelAssets().health_bar_gradient,
                stats.getRuntimeStat(StatComponent.RuntimeStat.health) / stats.getCurrentStat(StatComponent.BaseStat.maxHealth),
                shaderBatch, shader, cameraBar.combined);
        drawToBuffer(resourceBuffers.get(entityId), resourceBarColor, renderSystem.getLevelAssets().health_bar_gradient,
                stats.getRuntimeStat(StatComponent.RuntimeStat.resource) / stats.getCurrentStat(StatComponent.BaseStat.maxResource),
                shaderBatch, shader, cameraBar.combined);

        FrameBuffer buffer = renderSystem.getBuffers().get(entityId);
        buffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderSystem.getSpriteBatch().setProjectionMatrix(uiCamera.combined);
        renderSystem.getSpriteBatch().begin();
        //renderSystem.getFont().draw(renderSystem.getSpriteBatch(), String.format("%1.0f", stats.getRuntimeStat(StatComponent.RuntimeStat.actionPoints)), 0, buffer.getHeight());

        renderSystem.getSpriteBatch().draw(resourceBars.get(entityId), 50 - UI_WIDTH / 2, -UI_HEIGHT / 2 + RESOURCE_OFFSET, 175, 75 / 2);
        renderSystem.getSpriteBatch().draw(healthBars.get(entityId), 50 - UI_WIDTH / 2, -30, 200, 100 / 2);
        renderSystem.getFont().draw(renderSystem.getSpriteBatch(), String.format("%1.0f", stats.getRuntimeStat(StatComponent.RuntimeStat.actionPoints)),
                -UI_WIDTH / 2 - 8, 20);
        renderSystem.getSpriteBatch().end();
        buffer.end();

        Decal uiDecal = renderSystem.getUiMap().get(entityId);
        uiDecal.setPosition(physic.body.getPosition().x, physic.body.getPosition().y + 0.5f, uiDecal.getZ());
    }

    @Override
    public void removed(int entityId, RenderSystem renderSystem) {
        renderSystem.getBuffers().get(entityId).dispose();
        renderSystem.getBuffers().remove(entityId);
        renderSystem.getDecalMap().remove(entityId);
        renderSystem.getUiMap().remove(entityId);
        healthBuffers.get(entityId).dispose();
        resourceBuffers.get(entityId).dispose();
        healthBuffers.remove(entityId);
        resourceBuffers.remove(entityId);
        resourceBars.remove(entityId);
        healthBars.remove(entityId);
    }

    @Override
    public String defaultRenderState() {
        return PlayerRenderState.IDLE.name();
    }

    public enum PlayerRenderState {
        IDLE, MOVE_SIDE, ATTACK, MOVE_UP, MOVE_DOWN, TAUNT, ABILITY_1, ABILITY_2, ABILITY_3, ABILITY_4, TRAIT
    }
}
