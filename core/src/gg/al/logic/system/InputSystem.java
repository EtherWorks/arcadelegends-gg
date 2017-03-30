package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import gg.al.logic.component.Input;
import gg.al.logic.component.Position;
import gg.al.logic.map.LogicMap;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class InputSystem extends IteratingSystem {

    private final LogicMap logicMap;
    private ComponentMapper<Position> mapperPosition;
    private ComponentMapper<Input> mapperInput;

    public InputSystem(LogicMap logicMap) {
        super(Aspect.all(Input.class, Position.class));
        this.logicMap = logicMap;
    }

    @Override
    protected void process(int entityId) {
        Position pos = mapperPosition.get(entityId);
        Input input = mapperInput.get(entityId);
        if (!input.move.equals(Vector2.Zero)) {
            if (logicMap.inBounds(pos.position, input.move))
                pos.translate(input.move);
            input.move.set(Vector2.Zero);
        }
    }
}
