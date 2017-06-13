package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.PhysicComponent;
import gg.al.logic.component.PositionComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 31.03.2017.<br>
 * {@link IteratingSystem} responsible for updating the {@link PositionComponent} to match the dynamic position of the {@link PhysicComponent}.
 */
@Slf4j
public class PhysicPositionSystem extends IteratingSystem {

    private ComponentMapper<PhysicComponent> mapperPhysicComponent;
    private ComponentMapper<PositionComponent> mapperPosition;

    public PhysicPositionSystem() {
        super(Aspect.all(PositionComponent.class, PhysicComponent.class));
    }

    @Override
    protected void process(int entityId) {
        PhysicComponent physic = mapperPhysicComponent.get(entityId);

        PositionComponent position = mapperPosition.get(entityId);
        if (position.resetPos) {
            physic.body.setTransform(position.position, physic.body.getAngle());
            position.resetPos = false;
        } else
            position.set(Math.round(physic.body.getPosition().x),
                    Math.round(physic.body.getPosition().y));
        //log.debug(position.position.toString());
    }
}
