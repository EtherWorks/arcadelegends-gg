package gg.al.graphics.renderer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.utils.Array;
import gg.al.logic.component.PhysicComponent;
import gg.al.logic.component.PositionComponent;
import gg.al.logic.component.RenderComponent;
import gg.al.logic.system.RenderSystem;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Created by Thomas Neumann on 31.05.2017.<br>
 * Renderer for {@link gg.al.logic.entity.Entities#Bullet} entities.
 */
@Slf4j
public class BulletRenderer implements RenderComponent.RenderDelegate {
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
    }

    /**
     * Sets the current position of the rendered {@link Decal}, and the rotation given by the bullet direction.
     *
     * @param entityId     the bullet id
     * @param renderSystem the delegating {@link RenderSystem}
     */
    @Override
    public void process(int entityId, RenderSystem renderSystem) {
        PhysicComponent physic = renderSystem.getMapperPhysic().get(entityId);
        RenderComponent render = renderSystem.getMapperRender().get(entityId);


        float deg = (float) (Math.atan2(physic.body.getLinearVelocity().y, physic.body.getLinearVelocity().x) * 180.0 / Math.PI);
        Decal decal = renderSystem.getDecalMap().get(entityId);
        decal.setRotationZ(deg);
        float stateTime = renderSystem.getStateTime();
        TextureRegion region = render.getCurrentKeyFrame(stateTime);
        region.flip(region.isFlipX() ^ render.flipX, region.isFlipY() ^ render.flipY);
        decal.setTextureRegion(region);
        RenderComponent.RenderTemplate.AnimationTemplate template = render.getCurrentAnimation();
        decal.setWidth(template.width);
        decal.setHeight(template.height);
        decal.setPosition(physic.body.getPosition().x, physic.body.getPosition().y, decal.getZ());
    }

    @Override
    public void removed(int entityId, RenderSystem renderSystem) {
        renderSystem.getDecalMap().remove(entityId);
    }


    @Override
    public String defaultRenderState() {
        return "BULLET";
    }
}
