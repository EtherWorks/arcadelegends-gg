package gg.al.logic.component.data;

import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.component.StatComponent;

/**
 * Created by Thomas Neumann on 31.05.2017.<br />
 */
public class Item implements IStatAffect, ITickable {

    public ObjectMap<StatComponent.BaseStat, Float> flatStats = new ObjectMap<>();

    public ObjectMap<StatComponent.BaseStat, Float> percentageStats = new ObjectMap<>();

    public ObjectMap<StatComponent.BaseStat, Float> percentageBaseStats = new ObjectMap<>();

    public String name = "";

    public TickHandler tickHandler;

    private Item(ObjectMap<StatComponent.BaseStat, Float> flatStats, ObjectMap<StatComponent.BaseStat, Float> percentageStats, ObjectMap<StatComponent.BaseStat, Float> percentageBaseStats, String name, TickHandler tickHandler) {
        this.flatStats = flatStats;
        this.percentageStats = percentageStats;
        this.percentageBaseStats = percentageBaseStats;
        this.name = name;
        this.tickHandler = tickHandler;
    }

    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    @Override
    public void applyValue(StatComponent stats) {
        for (ObjectMap.Entry<StatComponent.BaseStat, Float> flatChange : flatStats.entries()) {
            stats.addCurrentStat(flatChange.key, flatChange.value);
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

    public static class ItemBuilder {

        public ObjectMap<StatComponent.BaseStat, Float> flatStats = new ObjectMap<>();

        public ObjectMap<StatComponent.BaseStat, Float> percentageStats = new ObjectMap<>();

        public ObjectMap<StatComponent.BaseStat, Float> percentageBaseStats = new ObjectMap<>();

        public String name = "";

        public TickHandler tickHandler;

        private ItemBuilder() {

        }

        public ItemBuilder flatStat(StatComponent.BaseStat stat, Float value) {
            flatStats.put(stat, value);
            return this;
        }

        public ItemBuilder percentageStat(StatComponent.BaseStat stat, Float value) {
            percentageStats.put(stat, value);
            return this;
        }

        public ItemBuilder percentageBaseStat(StatComponent.BaseStat stat, Float value) {
            percentageBaseStats.put(stat, value);
            return this;
        }

        public ItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ItemBuilder tickHandler(TickHandler tickHandler) {
            this.tickHandler = tickHandler;
            return this;
        }

        public Item build() {
            return new Item(new ObjectMap<>(flatStats), new ObjectMap<>(percentageStats), new ObjectMap<>(percentageBaseStats), name, tickHandler);
        }
    }
}
