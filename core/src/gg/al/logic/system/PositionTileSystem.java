package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.Position;
import gg.al.logic.map.LogicMap;
import gg.al.logic.map.Tile;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class PositionTileSystem extends IteratingSystem{

    private final LogicMap logicMap;
    private ComponentMapper<Position> mapperPosition;

    public PositionTileSystem(LogicMap logicMap) {
        super(Aspect.all(Position.class));
        this.logicMap = logicMap;
    }

    @Override
    protected void process(int entityId) {
        Position position = mapperPosition.get(entityId);

        Tile newTile = logicMap.getTile(position.position);
        if (position.tile != newTile) {
            position.tile.removeEntity(entityId);
            newTile.addEntity(entityId);
            position.set(newTile);
        }
    }
}
