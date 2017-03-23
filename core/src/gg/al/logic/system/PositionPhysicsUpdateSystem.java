package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
public class PositionPhysicsUpdateSystem extends IteratingSystem {

    public PositionPhysicsUpdateSystem()
    {
        super(Aspect.all());
    }

    @Override
    protected void process(int entityId) {

    }
}
