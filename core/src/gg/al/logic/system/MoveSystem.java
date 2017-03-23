package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.Move;
import gg.al.logic.component.Position;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
public class MoveSystem extends IteratingSystem {

    public MoveSystem()
    {
        super(Aspect.all(Position.class, Move.class));
    }

    @Override
    protected void process(int entityId) {

    }
}
