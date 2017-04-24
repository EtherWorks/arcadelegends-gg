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
            if (stats.actionPoints >= 1) {
                //anfangen sich zu bewegen
                Vector2 dir = vectorPool.obtain();
                Vector2 speed = vectorPool.obtain();
                if (Math.abs(input.move.x - pos.position.x) > Math.abs(input.move.y - pos.position.y))
                    dir.set(Math.signum(input.move.x - pos.position.x), 0);
                else
                    dir.set(0, Math.signum(input.move.y - pos.position.y));


                dynamicPhysic.getBody().setLinearVelocity(speed.set(dir).scl(stats.moveSpeed));
                input.stepMove.set(dir.add(pos.position));
                input.startToEnd.set(dir.set(pos.position).sub(input.stepMove));
                vectorPool.free(dir);
                vectorPool.free(speed);
                stats.actionPoints -= 1;
            }
        } else if (!dynamicPhysic.getBody().getLinearVelocity().equals(Vector2.Zero)) {
            //Testen ob an oder über stepmove, dann stoppen
            Vector2 curr = vectorPool.obtain();

            curr.set(dynamicPhysic.getBody().getPosition()).sub(input.stepMove);

            if (curr.dot(input.startToEnd) < 0) {
                dynamicPhysic.getBody().setLinearVelocity(Vector2.Zero);
                dynamicPhysic.getBody().setTransform(pos.position, dynamicPhysic.getBody().getAngle());
            }
            vectorPool.free(curr);
        }

    }

}
