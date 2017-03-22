package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import gg.al.game.AL;
import gg.al.logic.entity.component.Physics;
import gg.al.logic.entity.component.Render;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 22.03.2017.
 */
public class RenderSystem extends IteratingSystem {

    private final Map<Integer, Decal> decalMap;
    private final DecalBatch batch;
    private final float worldDegree;
    private ComponentMapper<Physics> physicsComponentMapper;
    private ComponentMapper<Render> renderComponentMapper;
    private float plusZ;

    public RenderSystem(DecalBatch batch, float worldDegree) {
        super(Aspect.all(Physics.class, Render.class));
        this.decalMap = new HashMap<>();
        this.batch = batch;
        this.worldDegree = worldDegree;
        Matrix4 rotMatrix = new Matrix4().rotate(Vector3.X, worldDegree);
        Vector3 vec = new Vector3(.5f, .5f, 0);
        vec.mul(rotMatrix);
        plusZ = vec.z;
    }

    @Override
    protected void process(int entityId) {
        Physics ph = physicsComponentMapper.get(entityId);
        Render rend = renderComponentMapper.get(entityId);

        if (!decalMap.containsKey(entityId)) {
            Decal d;
            decalMap.put(entityId, d = Decal.newDecal(rend.width, rend.heigth, new TextureRegion(AL.asset.get(rend.texture)), rend.transparent));
            if (rend.rotate)
                d.rotateX(worldDegree);
        }

        Decal d = decalMap.get(entityId);
        d.setPosition(new Vector3(ph.body.getPosition(), rend.rotate ? plusZ : .01f));
        batch.add(d);
    }
}
