package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import gg.al.logic.component.DynamicPhysic;
import gg.al.logic.component.Input;
import gg.al.logic.component.Position;
import gg.al.logic.component.Stats;
import gg.al.util.VectorUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
@Slf4j
public class InputSystem extends IteratingSystem {

    private final World world;
    private ComponentMapper<Position> mapperPosition;
    private ComponentMapper<Input> mapperInput;
    private ComponentMapper<DynamicPhysic> mapperDynamicPhysic;
    private ComponentMapper<Stats> mapperStats;

    private Pool<Vector2> vectorPool;

    public InputSystem(World world) {
        super(Aspect.all(Input.class, Position.class, DynamicPhysic.class, Stats.class));
        this.world = world;
        vectorPool = new Pool<Vector2>() {
            @Override
            protected Vector2 newObject() {
                return new Vector2();
            }

            @Override
            protected void reset(Vector2 object) {
                object.set(0, 0);
            }
        };
    }


    @Override
    protected void process(int entityId) {
        Position pos = mapperPosition.get(entityId);
        Input input = mapperInput.get(entityId);
        DynamicPhysic dynamicPhysic = mapperDynamicPhysic.get(entityId);
        Stats stats = mapperStats.get(entityId);
        if (input.move.dst(pos.position) != 0 && dynamicPhysic.getBody().getLinearVelocity().equals(Vector2.Zero)) {
            Vector2 to = vectorPool.obtain();
            if (Math.abs(input.move.x - pos.position.x) > Math.abs(input.move.y - pos.position.y))
                to.set(Math.signum(input.move.x - pos.position.x), 0);
            else
                to.set(0, Math.signum(input.move.y - pos.position.y));
            dynamicPhysic.getBody().setLinearVelocity(to.scl(stats.moveSpeed));
            input.stepMove.set(to.add(pos.position));
            vectorPool.free(to);
        } else if (!input.stepMove.equals(Vector2.Zero)) {
            Vector2 vec = vectorPool.obtain();

            if (input.lastDist < VectorUtil.sqrMag(vec.set(input.stepMove).sub(dynamicPhysic.getBody().getPosition()))) {
                dynamicPhysic.getBody().setLinearVelocity(0, 0);
                dynamicPhysic.getBody().setTransform(pos.position, dynamicPhysic.getBody().getAngle());
                input.lastDist = Double.MAX_VALUE;
                input.stepMove.setZero();
            } else
                input.lastDist = VectorUtil.sqrMag(vec.set(input.stepMove).sub(dynamicPhysic.getBody().getPosition()));
            vectorPool.free(vec);
        } else if (!dynamicPhysic.getBody().getLinearVelocity().equals(Vector2.Zero)) {
            dynamicPhysic.getBody().setLinearVelocity(0, 0);
            dynamicPhysic.getBody().setTransform(pos.position, dynamicPhysic.getBody().getAngle());
            input.lastDist = Double.MAX_VALUE;
        }
        //log.debug("{} {}", VectorUtil.magni(dynamicPhysic.getBody().getLinearVelocity()), input.move.dst(dynamicPhysic.getBody().getPosition()));

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
