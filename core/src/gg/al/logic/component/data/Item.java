package gg.al.logic.component.data;

import gg.al.logic.component.StatComponent;
import lombok.Builder;
import lombok.Singular;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 31.05.2017.<br />
 */
@Builder
public class Item implements IStatAffect, ITickable {

    @Singular
    public Map<StatComponent.BaseStat, Float> flatStats = new HashMap<>();

    @Singular
    public Map<StatComponent.BaseStat, Float> percentageStats = new HashMap<>();

    @Builder.Default
    public String name = "";

    public TickHandler tickHandler;

    @Override
    public void applyValue(StatComponent stats) {
        for (Map.Entry<StatComponent.BaseStat, Float> flatChange : flatStats.entrySet()) {
            stats.setCurrentStat(flatChange.getKey(), stats.getBaseStat(flatChange.getKey()) + flatChange.getValue());
        }
    }

    @Override
    public void applyPercentage(StatComponent stats) {
        for (Map.Entry<StatComponent.BaseStat, Float> percentageChange : percentageStats.entrySet()) {
            float value = stats.getCurrentStat(percentageChange.getKey());
            stats.setCurrentStat(percentageChange.getKey(), value + value * percentageChange.getValue());
        }
    }

    @Override
    public TickHandler getTickHandler() {
        return tickHandler;
    }
}
