package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.entity.component.Position;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 21.03.2017.<br />
 */
@Slf4j
public class TestSystem extends IteratingSystem {

    public TestSystem() {
        super(Aspect.one(Position.class));
    }

    @Override
    protected void process(int entityId) {
        Position pos = world.getEntity(entityId).getComponent(Position.class);
        log.debug("{}: {}", entityId, pos.position);
    }
}
