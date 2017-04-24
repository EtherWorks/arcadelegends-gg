package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import gg.al.logic.component.Stats;

/**
 * Created by Thomas Neumann on 24.04.2017.<br />
 */
public class StatSystem extends IntervalIteratingSystem {

    private ComponentMapper<Stats> mapperStats;

    public StatSystem() {
        super(Aspect.all(Stats.class), .5f);
    }

    @Override
    protected void process(int entityId) {
        Stats stats = mapperStats.get(entityId);

        if(stats.actionPoints + stats.actionPointRegen /2 <= stats.maxActionPoints)
            stats.actionPoints += stats.actionPointRegen/2;
        else
            stats.actionPoints = stats.maxActionPoints;

        if(stats.health + stats.healthRegen /2 <= stats.maxHealth)
            stats.health += stats.healthRegen/2;
        else
            stats.health = stats.maxHealth;

        if(stats.resource + stats.resourceRegen /2 <= stats.maxResource)
            stats.resource += stats.resourceRegen/2;
        else
            stats.resource = stats.maxResource;
    }
}
