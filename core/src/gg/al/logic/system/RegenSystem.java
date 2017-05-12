package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import gg.al.logic.component.StatComponent;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 */
public class RegenSystem extends IntervalIteratingSystem {

    private ComponentMapper<StatComponent> mapperStatComponent;

    public RegenSystem(float interval) {
        super(Aspect.all(StatComponent.class), interval);
    }

    @Override
    protected void process(int entityId) {
        StatComponent stats = mapperStatComponent.get(entityId);
        float interval = getIntervalDelta();
        if (stats.getFlagStat(StatComponent.FlagStat.dead))
            return;

        regen(stats, StatComponent.RuntimeStat.actionPoints,
                StatComponent.BaseStat.actionPointsRegen,
                StatComponent.BaseStat.maxActionPoints,
                interval);

        regen(stats, StatComponent.RuntimeStat.health,
                StatComponent.BaseStat.healthRegen,
                StatComponent.BaseStat.maxHealth,
                interval);

        regen(stats, StatComponent.RuntimeStat.resource,
                StatComponent.BaseStat.resourceRegen,
                StatComponent.BaseStat.maxResource,
                interval);

    }

    private static void regen(StatComponent stats, StatComponent.RuntimeStat stat, StatComponent.BaseStat regen, StatComponent.BaseStat max, float interval) {
        if (stats.getRuntimeStat(stat) +
                stats.getCurrentStat(regen) *
                        interval <= stats.getCurrentStat(max))
            stats.addRuntimeStat(stat,
                    stats.getCurrentStat(regen) * interval);
        else
            stats.setRuntimeStat(stat,
                    stats.getCurrentStat(max));
    }
}
