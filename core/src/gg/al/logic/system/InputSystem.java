package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import gg.al.logic.component.DynamicPhysic;
import gg.al.logic.component.Input;
import gg.al.logic.component.KinematicPhysic;
import gg.al.logic.component.Position;
import gg.al.logic.map.LogicMap;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class InputSystem extends IteratingSystem {

    private final LogicMap logicMap;
    private ComponentMapper<Position> mapperPosition;
    private ComponentMapper<Input> mapperInput;
    private ComponentMapper<KinematicPhysic> mapperKinetmaticPhysic;
    private ComponentMapper<DynamicPhysic> mapperDynamicPhysic;

    public InputSystem(LogicMap logicMap) {
        super(Aspect.all(Input.class, Position.class));
        this.logicMap = logicMap;
    }

    @Override
    protected void process(int entityId) {
        Position pos = mapperPosition.get(entityId);
        Input input = mapperInput.get(entityId);
        KinematicPhysic kinematicPhysic = mapperKinetmaticPhysic.get(entityId);
        if (!input.move.equals(Vector2.Zero)) {
            if (logicMap.inBounds(pos.position, input.move)) {
                IntSet entities = logicMap.getTile(Math.round(pos.position.x + input.move.x), Math.round(pos.position.y + input.move.y)).getEntities();
                boolean move = true;
                while (entities.iterator().hasNext) {
                    int entity = entities.iterator().next();
                    DynamicPhysic dynamicPhysic = mapperDynamicPhysic.get(entity);
                    if (dynamicPhysic != null) {
                        move = false;
                        break;
                    }
                }
                entities.iterator().reset();
                if (move)
                    pos.translate(input.move);
            }
            input.move.set(Vector2.Zero);
        }
        if (kinematicPhysic != null)
            kinematicPhysic.getBody().setTransform(pos.position, kinematicPhysic.getBody().getAngle());
    }
}
