package gg.al.logic.component.data;

import gg.al.logic.component.StatComponent;
import lombok.Builder;
import lombok.Singular;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 */
@Builder
public class StatusEffect {

    public float effectTime;
    @Builder.Default
    public float remainingTime = -1;

    @Singular
    public Map<StatComponent.BaseStat, Float> flatStats = new HashMap<>();

    @Singular
    public Map<StatComponent.BaseStat, Float> percentageStats = new HashMap<>();

    public TickHandler tickHandler;


    public void applyValue(StatComponent stats) {
        for (Map.Entry<StatComponent.BaseStat, Float> flatChange : flatStats.entrySet()) {
            stats.setCurrentStat(flatChange.getKey(), stats.getBaseStat(flatChange.getKey()) + flatChange.getValue());
        }
    }

    public void applyPercentage(StatComponent stats) {
        for (Map.Entry<StatComponent.BaseStat, Float> percentageChange : percentageStats.entrySet()) {
            float value = stats.getCurrentStat(percentageChange.getKey());
            stats.setCurrentStat(percentageChange.getKey(), value + value * percentageChange.getValue());
        }
    }

    @FunctionalInterface
    public interface TickHandler {
        void onTick(float delta, StatComponent statComponent, StatusEffect effect);
    }
}
