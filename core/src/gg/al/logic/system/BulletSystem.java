package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.BulletComponent;
import gg.al.logic.component.PhysicComponent;
import gg.al.logic.component.PositionComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 29.05.2017.<br />
 */
@Slf4j
public class BulletSystem extends IteratingSystem {

    private ComponentMapper<BulletComponent> mapperBulletControlComponent;
    private ComponentMapper<PhysicComponent> mapperPhysicComponent;
    private ComponentMapper<PositionComponent> mapperPositionComponent;

    private ArcadeWorld arcadeWorld;

    public BulletSystem(ArcadeWorld arcadeWorld) {
        super(Aspect.all(BulletComponent.class, PhysicComponent.class));
        this.arcadeWorld = arcadeWorld;
    }

    @Override
    protected void process(int entityId) {
        BulletComponent bCon = mapperBulletControlComponent.get(entityId);
        if (bCon.delete) {
            if (bCon.deleteCallback != null)
                bCon.deleteCallback.onDelete();
            arcadeWorld.delete(entityId);
            return;
        }
        PhysicComponent phys = mapperPhysicComponent.get(entityId);
        bCon.currentDistance += phys.body.getPosition().dst(bCon.old);
        if (bCon.maxDistance <= bCon.currentDistance) {
            arcadeWorld.delete(entityId);
            return;
        }
        bCon.old.set(phys.body.getPosition());

        if (bCon.target != -1) {
            PositionComponent positionComponent = mapperPositionComponent.get(bCon.target);
            if(positionComponent != null) {
                Vector2 targetPos = positionComponent.position;
                Vector2 bulPos = phys.body.getPosition();
                bCon.move.set(targetPos).sub(bulPos).nor();
            }
            else
                bCon.target = -1;
        }
        phys.body.setLinearVelocity(bCon.move.x * bCon.speed, bCon.move.y * bCon.speed);
    }
}
