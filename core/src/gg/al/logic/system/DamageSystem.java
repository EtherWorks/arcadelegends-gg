package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.Damage;
import gg.al.logic.component.Stats;

/**
 * Created by Thomas Neumann on 25.04.2017.<br />
 */
public class DamageSystem extends IteratingSystem {

    private ComponentMapper<Stats> mapperStats;
    private ComponentMapper<Damage> mapperDamage;

    public DamageSystem() {
        super(Aspect.all(Damage.class, Stats.class));
    }

    @Override
    protected void process(int entityId) {
        Stats stats = mapperStats.get(entityId);
        if (stats.dead)
            return;
        Damage damage = mapperDamage.get(entityId);

        float amount = damage.amount;
        switch (damage.damageType) {
            case True:
                break;
            case Magic:
                float mr = stats.magicResist - stats.magicResist * damage.penetration / 100;
                amount = amount - amount * mr / 100;
                break;
            case Normal:
                float ar = stats.armor - stats.armor * damage.penetration / 100;
                amount = amount - amount * ar / 100;
                break;
        }
        if (stats.health - amount <= 0) {
            stats.health = 0;
            stats.resource = 0;
            stats.actionPoints = 0;
            stats.dead = true;
        } else
            stats.health -= amount;
        mapperDamage.remove(entityId);
    }
}
