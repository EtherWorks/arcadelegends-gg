package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.DynamicPhysic;
import gg.al.logic.component.Position;

/**
 * Created by Thomas Neumann on 31.03.2017.
 */
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

        position.set(dynamicPhysic.getBody().getPosition());
    }
}
