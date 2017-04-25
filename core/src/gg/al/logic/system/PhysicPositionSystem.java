package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 31.03.2017.
 */
@Slf4j
public class PhysicPositionSystem extends IteratingSystem {

    private ComponentMapper<DynamicPhysic> mapperDynamicPhysic;
    private ComponentMapper<KinematicPhysic> mapperKinematicPhysic;
    private ComponentMapper<Position> mapperPosition;

    public PhysicPositionSystem() {
        super(Aspect.all(Position.class).one(DynamicPhysic.class, KinematicPhysic.class));
    }

    @Override
    protected void process(int entityId) {
        DynamicPhysic dynamicPhysic = mapperDynamicPhysic.get(entityId);
        KinematicPhysic kinematicPhysic = mapperKinematicPhysic.get(entityId);
        IPhysic physic = dynamicPhysic == null ? kinematicPhysic : dynamicPhysic;

        Position position = mapperPosition.get(entityId);
        if (position.resetPos){
            physic.getBody().setTransform(position.position, physic.getBody().getAngle());
            position.resetPos = false;
        }
        else
        position.set(Math.round(physic.getBody().getPosition().x),
                Math.round(physic.getBody().getPosition().y));
        //log.debug(position.position.toString());
    }
}
