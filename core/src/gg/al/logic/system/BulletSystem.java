package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.BulletComponent;
import gg.al.logic.component.PhysicComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 29.05.2017.<br />
 */
@Slf4j
public class BulletSystem extends IteratingSystem {

    private ComponentMapper<BulletComponent> mapperBulletControlComponent;
    private ComponentMapper<PhysicComponent> mapperPhysicComponent;

    private ArcadeWorld arcadeWorld;

    public BulletSystem(ArcadeWorld arcadeWorld) {
        super(Aspect.all(BulletComponent.class, PhysicComponent.class));
        this.arcadeWorld = arcadeWorld;
    }

    @Override
    protected void process(int entityId) {
        BulletComponent bCon = mapperBulletControlComponent.get(entityId);
        if (bCon.delete) {
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
        phys.body.setLinearVelocity(bCon.move);
    }
}
