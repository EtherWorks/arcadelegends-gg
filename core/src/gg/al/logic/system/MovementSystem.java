package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.entity.component.Movement;
import gg.al.logic.entity.component.Physics;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 21.03.2017.<br />
 */
@Slf4j
public class MovementSystem extends IteratingSystem {

    public MovementSystem() {
        super(Aspect.all(Movement.class, Physics.class));
    }

    @Override
    protected void process(int entityId) {
        Movement mov = world.getEntity(entityId).getComponent(Movement.class);
        Physics physics = world.getEntity(entityId).getComponent(Physics.class);
        mov.time += world.getDelta();
        if (mov.time > mov.stepTime) {
            mov.time = 0;
            physics.body.setTransform(physics.body.getPosition().add(mov.direction), physics.body.getAngle());
            log.debug("{}: {}", entityId, physics.body.getPosition());
        }
    }
}
