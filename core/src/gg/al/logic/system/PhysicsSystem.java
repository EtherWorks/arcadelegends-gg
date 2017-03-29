package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.Physic;
import gg.al.logic.component.Position;

/**
 * Created by Thomas Neumann on 24.03.2017.<br />
 */
public class PhysicsSystem extends IteratingSystem{

    private ComponentMapper<Physic> mPhysic;
    private ComponentMapper<Position> mPosition;

    public PhysicsSystem()
    {
        super(Aspect.all(Physic.class));
    }

    @Override
    protected void process(int entityId) {
        Physic ph = mPhysic.get(entityId);
        Position pos = mPosition.get(entityId);
        if(pos != null)
            pos.position.set(ph.body.getPosition());
    }
}
