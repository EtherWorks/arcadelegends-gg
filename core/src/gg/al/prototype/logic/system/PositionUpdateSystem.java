package gg.al.prototype.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.prototype.logic.entity.component.Physics;
import gg.al.prototype.logic.entity.component.PositionUpdate;

/**
 * Created by Thomas Neumann on 21.03.2017.
 */
public class PositionUpdateSystem extends IteratingSystem {

    private ComponentMapper<PositionUpdate> positionUpdateMapper;
    private ComponentMapper<Physics> physicsMapper;

    public PositionUpdateSystem() {
        super(Aspect.all(Physics.class, PositionUpdate.class));
    }

    @Override
    protected void process(int entityId) {
        Physics ph = physicsMapper.get(entityId);
        PositionUpdate update = positionUpdateMapper.get(entityId);

        ph.body.setTransform(update.futurePosition, ph.body.getAngle());

        positionUpdateMapper.remove(entityId);
    }
}
