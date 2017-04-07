package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.DynamicPhysic;
import gg.al.logic.component.Input;
import gg.al.logic.component.Position;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 31.03.2017.
 */
@Slf4j
public class PhysicPositionSystem extends IteratingSystem {

    private ComponentMapper<DynamicPhysic> mapperDynamicPhysic;
    private ComponentMapper<Position> mapperPosition;

    public PhysicPositionSystem() {
        super(Aspect.all(Position.class, DynamicPhysic.class));
    }

    @Override
    protected void process(int entityId) {
        DynamicPhysic dynamicPhysic = mapperDynamicPhysic.get(entityId);
        Position position = mapperPosition.get(entityId);

        position.set(Math.round(dynamicPhysic.getBody().getPosition().x),
                Math.round(dynamicPhysic.getBody().getPosition().y));
        log.debug(position.position.toString());
    }
}
