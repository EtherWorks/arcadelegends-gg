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
    private ComponentMapper<DynamicPhysic> mapperDynamicPhysic;

    public InputSystem(LogicMap logicMap) {
        super(Aspect.all(Input.class, Position.class, DynamicPhysic.class));
        this.logicMap = logicMap;
    }

    @Override
    protected void process(int entityId) {
        Position pos = mapperPosition.get(entityId);
        Input input = mapperInput.get(entityId);
        DynamicPhysic dynamicPhysic = mapperDynamicPhysic.get(entityId);

        if(input.move.dst(pos.position) != 0)
        {
            Vector2 dir = Math.abs(input.move.x - pos.position.x) < Math.abs(input.move.y - pos.position.y) ?
                    Vector2.Zero : Vector2.Zero;
        }


//        System.out.println(dynamicPhysic.getBody().getLinearVelocity());
//        if (!input.move.equals(pos.position)) {
//            IntSet entities = logicMap.getTile(input.move).getEntities();
//            boolean move = true;
//            while (entities.iterator().hasNext) {
//                int entity = entities.iterator().next();
//                DynamicPhysic other = mapperDynamicPhysic.get(entity);
//                if (other != null) {
//                    move = false;
//                    break;
//                }
//            }
//            entities.iterator().reset();
//
//            if (move)
//                dynamicPhysic.getBody().setLinearVelocity(input.move.cpy().sub(pos.position).nor().scl(2));
//            else
//                input.move.set(pos.position);
//        } else if (dynamicPhysic.getBody().getPosition().dst(input.move) < 0.01f && !dynamicPhysic.getBody().getLinearVelocity().equals(Vector2.Zero)) {
//            dynamicPhysic.getBody().setTransform(pos.position, dynamicPhysic.getBody().getAngle());
//            dynamicPhysic.getBody().setLinearVelocity(Vector2.Zero);
//            pos.position.set(input.move);
//        }
    }

}
