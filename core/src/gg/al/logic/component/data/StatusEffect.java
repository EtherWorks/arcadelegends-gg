package gg.al.logic.component.data;

import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.component.StatComponent;

/**
 * Created by Thomas Neumann on 26.04.2017.<br>
 * Data class describing a status effect.
 */
public class StatusEffect implements IStatAffect, ITickable {

    /**
     * The time this {@link StatusEffect} stays in effect.
     */
    public float effectTime;
    /**
     * The remaining time this {@link StatusEffect} stays in effect.
     */
    public float remainingTime = -1;

    /**
     * The flat stat changes.
     */
    public ObjectMap<StatComponent.BaseStat, Float> flatStats = new ObjectMap<>();

    /**
     * The percent stat changes
     */
    public ObjectMap<StatComponent.BaseStat, Float> percentageStats = new ObjectMap<>();

    /**
     * The percentage stat changes based on the base stats of an entity.
     */
    public ObjectMap<StatComponent.BaseStat, Float> percentageBaseStats = new ObjectMap<>();

    /**
     * The {@link gg.al.logic.component.data.ITickable.TickHandler} of this {@link StatusEffect}.
     */
    public TickHandler tickHandler;

    public StatusEffect(float effectTime, float remainingTime, ObjectMap<StatComponent.BaseStat, Float> flatStats, ObjectMap<StatComponent.BaseStat, Float> percentageStats, ObjectMap<StatComponent.BaseStat, Float> percentageBaseStats, TickHandler tickHandler) {
        this.effectTime = effectTime;
        this.remainingTime = remainingTime;
        this.flatStats = flatStats;
        this.percentageStats = percentageStats;
        this.percentageBaseStats = percentageBaseStats;
        this.tickHandler = tickHandler;
    }

    public static StatusEffectBuilder builder() {
        return new StatusEffectBuilder();
    }

    @Override
    public void applyValue(StatComponent stats) {
        for (ObjectMap.Entry<StatComponent.BaseStat, Float> flatChange : flatStats.entries()) {
            stats.setCurrentStat(flatChange.key, stats.getBaseStat(flatChange.key) + flatChange.value);
        }
    }

    @Override
    public void applyPercentage(StatComponent stats) {
        for (ObjectMap.Entry<StatComponent.BaseStat, Float> percentageChange : percentageStats.entries()) {
            float value = stats.getCurrentStat(percentageChange.key);
            stats.setCurrentStat(percentageChange.key, value + value * percentageChange.value);
        }
    }

    @Override
    public void applyBasePercentage(StatComponent stats) {
        for (ObjectMap.Entry<StatComponent.BaseStat, Float> percentageBaseChange : percentageBaseStats.entries()) {
            float value = stats.getBaseStat(percentageBaseChange.key);
            stats.setCurrentStat(percentageBaseChange.key, stats.getCurrentStat(percentageBaseChange.key) + value * percentageBaseChange.value);
        }
    }

    @Override
    public TickHandler getTickHandler() {
        return tickHandler;
    }

    public static class StatusEffectBuilder {

        public float effectTime;
        public float remainingTime = -1;

        public ObjectMap<StatComponent.BaseStat, Float> flatStats = new ObjectMap<>();

        public ObjectMap<StatComponent.BaseStat, Float> percentageStats = new ObjectMap<>();

        public ObjectMap<StatComponent.BaseStat, Float> percentageBaseStats = new ObjectMap<>();

        public TickHandler tickHandler;

        private StatusEffectBuilder() {

        }

        public StatusEffectBuilder flatStat(StatComponent.BaseStat stat, Float value) {
            flatStats.put(stat, value);
            return this;
        }

        public StatusEffectBuilder percentageStat(StatComponent.BaseStat stat, Float value) {
            percentageStats.put(stat, value);
            return this;
        }

        public StatusEffectBuilder percentageBaseStat(StatComponent.BaseStat stat, Float value) {
            percentageBaseStats.put(stat, value);
            return this;
        }

        public StatusEffectBuilder tickHandler(TickHandler tickHandler) {
            this.tickHandler = tickHandler;
            return this;
        }

        public StatusEffectBuilder effectTime(float effectTime) {
            this.effectTime = effectTime;
            return this;
        }

        public StatusEffectBuilder remainingTime(float remainingTime) {
            this.remainingTime = remainingTime;
            return this;
        }

        public StatusEffect build() {
            return new StatusEffect(effectTime, remainingTime, new ObjectMap<>(flatStats), new ObjectMap<>(percentageStats), new ObjectMap<>(percentageBaseStats), tickHandler);
        }
    }
}
