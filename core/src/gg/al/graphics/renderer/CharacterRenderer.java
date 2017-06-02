package gg.al.graphics.renderer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import gg.al.game.AL;
import gg.al.logic.component.*;
import gg.al.logic.system.RenderSystem;

import java.util.Map;

/**
 * Created by Thomas Neumann on 29.05.2017.<br />
 */
public class CharacterRenderer implements RenderComponent.RenderDelegate {

    @Override
    public void inserted(int entityId, RenderSystem renderSystem) {
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
                render.animations.put(entry.getKey(), new Animation<TextureRegion>(entry.getValue().frameDuration, new Array<>(frames), Animation.PlayMode.LOOP));
            }
        }
        Decal decal = Decal.newDecal(render.width, render.height, render.getCurrentKeyFrame(renderSystem.getStateTime()), render.transparent);
        decal.rotateX(renderSystem.getWorldDegree());
        decal.setPosition(position.position.x, position.position.y, 0);
        renderSystem.getDecalMap().put(entityId, decal);

        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, renderSystem.getBuffWidth(), renderSystem.getBuffHeight(), false);
        buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        renderSystem.getBuffers().put(entityId, buffer);

        TextureRegion uiTextureRegion = new TextureRegion(buffer.getColorBufferTexture());
        uiTextureRegion.flip(false, true);
        Decal uiDecal = Decal.newDecal(3, 5, uiTextureRegion, true);
        uiDecal.setPosition(position.position.x, position.position.y, 1);
        renderSystem.getUiMap().put(entityId, uiDecal);
    }

    @Override
    public void process(int entityId, RenderSystem renderSystem) {
        PhysicComponent physic = renderSystem.getMapperPhysic().get(entityId);
        PositionComponent position = renderSystem.getMapperPosition().get(entityId);
        RenderComponent render = renderSystem.getMapperRender().get(entityId);
        StatComponent stats = renderSystem.getMapperStats().get(entityId);

        CharacterComponent character = renderSystem.getMapperCharacterComponent().get(entityId);
        Decal decal = renderSystem.getDecalMap().get(entityId);
        float stateTime = renderSystem.getStateTime();
        if (character != null && render.isInState(PlayerRenderState.ATTACK)) {
            stateTime = render.animations.get(PlayerRenderState.ATTACK.name()).getAnimationDuration() * character.getRenderMultiplicator();
        }
        TextureRegion region = render.getCurrentKeyFrame(stateTime);
        region.flip(region.isFlipX() ^ render.flipX, region.isFlipY() ^ render.flipY);
        decal.setTextureRegion(region);
        RenderComponent.RenderTemplate.AnimationTemplate template = render.getCurrentAnimation();
        decal.setWidth(template.width);
        decal.setHeight(template.height);

        if (physic != null)
            decal.setPosition(physic.body.getPosition().x, physic.body.getPosition().y, decal.getZ());
        else
            decal.setPosition(position.position.x, position.position.y, decal.getZ());

        FrameBuffer buffer = renderSystem.getBuffers().get(entityId);
        buffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderSystem.getSpriteBatch().setProjectionMatrix(renderSystem.getUiCamera().combined);
        renderSystem.getSpriteBatch().begin();
        renderSystem.getFont().draw(renderSystem.getSpriteBatch(), stats.toString(), 0, renderSystem.getBuffHeight());
        renderSystem.getSpriteBatch().end();
        buffer.end();

        Decal uiDecal = renderSystem.getUiMap().get(entityId);
        uiDecal.setPosition(physic.body.getPosition().x + 2, physic.body.getPosition().y - 3, uiDecal.getZ());
    }

    @Override
    public void removed(int entityId, RenderSystem renderSystem) {
        renderSystem.getBuffers().get(entityId).dispose();
        renderSystem.getBuffers().remove(entityId);
        renderSystem.getDecalMap().remove(entityId);
        renderSystem.getUiMap().remove(entityId);
    }

    @Override
    public String defaultRenderState() {
        return PlayerRenderState.IDLE.name();
    }

    public enum PlayerRenderState {
        IDLE, MOVE_SIDE, ATTACK, MOVE_UP, MOVE_DOWN, TAUNT
    }
}
