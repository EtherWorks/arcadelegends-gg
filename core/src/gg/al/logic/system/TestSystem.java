package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.entity.component.Movement;
import gg.al.logic.entity.component.Physics;
import gg.al.logic.entity.component.Position;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 21.03.2017.<br />
 */
@Slf4j
public class TestSystem extends IteratingSystem {

    public TestSystem() {
        super(Aspect.all(Position.class, Movement.class, Physics.class));
    }

    @Override
    protected void process(int entityId) {
        Position pos = world.getEntity(entityId).getComponent(Position.class);
        Movement mov = world.getEntity(entityId).getComponent(Movement.class);
        Physics physics = world.getEntity(entityId).getComponent(Physics.class);
        mov.time += world.getDelta();
        if (mov.time > mov.stepTime) {
            mov.time = 0;
            pos.position.add(mov.direction);
            physics.body.setTransform(pos.position, physics.body.getAngle());
            log.debug("{}: {}", entityId, pos.position);
        }
    }
}
