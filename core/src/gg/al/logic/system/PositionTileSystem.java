package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.KinetmaticPhysic;
import gg.al.logic.component.Position;
import gg.al.logic.map.LogicMap;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class PositionTileSystem extends IteratingSystem{

    private final LogicMap logicMap;
    private ComponentMapper<Position> mapperPosition;
    private ComponentMapper<KinetmaticPhysic> mapperKinetmaticPhysic;

    public PositionTileSystem(LogicMap logicMap) {
        super(Aspect.all(Position.class));
        this.logicMap = logicMap;
    }

    @Override
    protected void process(int entityId) {
        Position position = mapperPosition.get(entityId);
        KinetmaticPhysic kinetmaticPhysic = mapperKinetmaticPhysic.get(entityId);
        position.set(logicMap.getTile(position.position));
        if(kinetmaticPhysic != null)
            kinetmaticPhysic.body.setTransform(position.position, kinetmaticPhysic.body.getAngle());
    }
}
