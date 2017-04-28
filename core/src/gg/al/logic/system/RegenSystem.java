package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import gg.al.logic.component.Stats;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 */
public class RegenSystem extends IntervalIteratingSystem {

    private ComponentMapper<Stats> mapperStats;

    public RegenSystem(float interval) {
        super(Aspect.all(Stats.class), interval);
    }

    @Override
    protected void process(int entityId) {
        Stats stats = mapperStats.get(entityId);
        float interval = getIntervalDelta();
        if (stats.dead)
            return;

        if (stats.actionPoints + stats.actionPointRegen * interval <= stats.maxActionPoints)
            stats.actionPoints += stats.actionPointRegen * interval;
        else
            stats.actionPoints = stats.maxActionPoints;

        if (stats.health + stats.healthRegen * interval <= stats.maxHealth)
            stats.health += stats.healthRegen * interval;
        else
            stats.health = stats.maxHealth;

        if (stats.resource + stats.resourceRegen * interval <= stats.maxResource)
            stats.resource += stats.resourceRegen * interval;
        else
            stats.resource = stats.maxResource;
    }
}