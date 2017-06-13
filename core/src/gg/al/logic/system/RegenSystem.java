package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import gg.al.logic.component.StatComponent;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 * {@link IntervalIteratingSystem} responsible for calculating second based regeneration, like health regeneration.
 */
public class RegenSystem extends IntervalIteratingSystem {

    private ComponentMapper<StatComponent> mapperStatComponent;

    public RegenSystem(float interval) {
        super(Aspect.all(StatComponent.class), interval);
    }

    /**
     * Calculates and sets the proper regen values for a {@link StatComponent}.
     *
     * @param stats    the {@link StatComponent} to be changed
     * @param stat     the {@link gg.al.logic.component.StatComponent.RuntimeStat} to be regenerated
     * @param regen    the {@link gg.al.logic.component.StatComponent.BaseStat} containing the regeneration value
     * @param max      the {@link gg.al.logic.component.StatComponent.BaseStat} containing the maximal value of the {@link gg.al.logic.component.StatComponent.RuntimeStat}
     * @param interval the passed time since the last interval
     */
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

    @Override
    protected void process(int entityId) {
        StatComponent stats = mapperStatComponent.get(entityId);
        float interval = getIntervalDelta();
        if (stats.getFlag(StatComponent.FlagStat.dead))
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
}
