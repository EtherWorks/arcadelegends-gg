package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import gg.al.logic.component.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 24.03.2017.<br />
 */
public class TestSystem extends IteratingSystem {

    private ComponentMapper<Position> positionComponentMapper;

    private DecalBatch decalBatch;
    private Pixmap map;

    private Map<Integer, Decal> decalMap;

    public TestSystem(DecalBatch decalBatch) {
        super(Aspect.all(Position.class));
        this.decalBatch = decalBatch;
        map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.setColor(Color.WHITE);
        map.fill();
        decalMap = new HashMap<>();
    }

    @Override
    protected void process(int entityId) {
        Position pos = positionComponentMapper.get(entityId);
        Decal d = decalMap.get(entityId);
        d.setPosition(pos.position.x, pos.position.y, 0.01f);
        decalBatch.add(d);
    }

    @Override
    protected void inserted(int entityId) {
        decalMap.put(entityId, Decal.newDecal(1, 1, new TextureRegion(new Texture(map))));
    }

    @Override
    protected void removed(int entityId) {
        decalMap.remove(entityId);
    }
}
