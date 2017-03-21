package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.entity.component.Physics;
import gg.al.logic.entity.component.Position;
import gg.al.logic.entity.component.PositionUpdate;

/**
 * Created by Thomas Neumann on 21.03.2017.
 */
public class PositionUpdateSystem extends IteratingSystem {

    private ComponentMapper<Position> positionMapper;
    private ComponentMapper<PositionUpdate> positionUpdateMapper;
    private ComponentMapper<Physics> physicsMapper;

    public PositionUpdateSystem() {
        super(Aspect.all(Position.class, PositionUpdate.class).one(Position.class, Physics.class));
    }

    @Override
    protected void process(int entityId) {
        Position pos = positionMapper.get(entityId);
        PositionUpdate update = positionUpdateMapper.get(entityId);

        pos.position.set(update.futurePosition);

        Physics ph = physicsMapper.get(entityId);
        if (ph != null)
            ph.body.setTransform(update.futurePosition, ph.body.getAngle());

        positionUpdateMapper.remove(entityId);
    }
}
