package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.component.DynamicPhysic;
import gg.al.logic.component.Position;
import gg.al.logic.component.Render;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class RenderSystem extends IteratingSystem {

    private final ObjectMap<Integer, Decal> decalMap;
    private final ObjectMap<Integer, Decal> uiMap;
    private final DecalBatch decalBatch;
    private final AssetManager assetManager;
    private ComponentMapper<Render> mapperRender;
    private ComponentMapper<Position> mapperPosition;
    private ComponentMapper<DynamicPhysic> mapperDynamicPhysic;

    public RenderSystem(DecalBatch decalBatch, AssetManager assetManager) {
        super(Aspect.all(Render.class, Position.class));
        decalMap = new ObjectMap<>();
        uiMap = new ObjectMap<>();
        this.decalBatch = decalBatch;
        this.assetManager = assetManager;
    }

    @Override
    protected void process(int entityId) {
        DynamicPhysic dynamicPhysic = mapperDynamicPhysic.get(entityId);
        Position position = mapperPosition.get(entityId);
        Decal decal = decalMap.get(entityId);
        if (dynamicPhysic != null)
            decal.setPosition(dynamicPhysic.getBody().getPosition().x, dynamicPhysic.getBody().getPosition().y, 0);
        else
            decal.setPosition(position.position.x, position.position.y, 0);
        decalBatch.add(decal);
    }

    @Override
    protected void inserted(int entityId) {
        Render render = mapperRender.get(entityId);
        Position position = mapperPosition.get(entityId);
        Decal decal = Decal.newDecal(render.width, render.height, new TextureRegion(assetManager.get(render.texture)), render.transparent);
        decal.setPosition(position.position.x, position.position.y, 0);
        decalMap.put(entityId, decal);
    }

    @Override
    protected void removed(int entityId) {
        decalMap.remove(entityId);
    }
}
