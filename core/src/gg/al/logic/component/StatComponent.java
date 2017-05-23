package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.component.data.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 08.05.2017.<br />
 */
public class StatComponent extends PooledComponent implements ITemplateable {

    private final static String DELIM = "_";
    private final static String BASE = "Base";
    private final static String GROWTH = "Growth";
    private final static String GROWTH_PERCENTAGE = "GrowthPercentage";

    private final ObjectMap<FlagStat, Boolean> flagStats;
    private final ObjectMap<RuntimeStat, Float> runtimeStats;
    private final ObjectMap<BaseStat, Float> currentStat;
    private final ObjectMap<BaseStat, Float> baseStats;
    private final ObjectMap<BaseStat, Float> growthStats;
    private final ObjectMap<BaseStat, Float> growthPercentageStats;
    public final ObjectMap<String, StatusEffect> statusEffects;
    public final Array<Damage> damages;

    public StatComponent() {
        runtimeStats = new ObjectMap<>();
        currentStat = new ObjectMap<>();
        flagStats = new ObjectMap<>();
        baseStats = new ObjectMap<>();
        growthStats = new ObjectMap<>();
        growthPercentageStats = new ObjectMap<>();
        statusEffects = new ObjectMap<>();
        damages = new Array<>();
    }

    @Override
    protected void reset() {
        runtimeStats.clear();
        currentStat.clear();
        flagStats.clear();
        baseStats.clear();
        growthStats.clear();
        growthPercentageStats.clear();
        statusEffects.clear();
        damages.clear();
    }

    public void recalculateCurrent() {
        for (BaseStat stat : BaseStat.values()) {
            float value = baseStats.get(stat);
            value += runtimeStats.get(RuntimeStat.level) * growthStats.get(stat);
            value += value * growthPercentageStats.get(stat);
            currentStat.put(stat, value);
        }
        for (StatusEffect effect : statusEffects.values()) {
            effect.applyValue(this);
        }
        for (StatusEffect effect : statusEffects.values()) {
            effect.applyPercentage(this);
        }
    }

    public void setFlagStat(FlagStat stat, boolean flag) {
        flagStats.put(stat, flag);
    }

    public boolean getFlagStat(FlagStat stat) {
        return flagStats.get(stat);
    }


    public void setRuntimeStat(RuntimeStat runtimeStat, float value) {
        this.runtimeStats.put(runtimeStat, value);
    }

    public void addRuntimeStat(RuntimeStat runtimeStat, float value) {
        this.runtimeStats.put(runtimeStat, runtimeStats.get(runtimeStat) + value);
    }


    public float getRuntimeStat(RuntimeStat runtimeStat) {
        return this.runtimeStats.get(runtimeStat);
    }

    public void setCurrentStat(BaseStat stat, float value) {
        currentStat.put(stat, value);
    }

    public void addCurrentStat(BaseStat stat, float value) {
        currentStat.put(stat, currentStat.get(stat) + value);
    }

    public float getCurrentStat(BaseStat stat) {
        return currentStat.get(stat);
    }

    public void setBaseStat(BaseStat stat, float value) {
        baseStats.put(stat, value);
    }

    public float getBaseStat(BaseStat stat) {
        return baseStats.get(stat);
    }


    public void setGrowthStat(BaseStat stat, float value) {
        growthStats.put(stat, value);
    }

    public float getGrowthStat(BaseStat stat) {
        return growthStats.get(stat);
    }

    public void setGrowthPercStat(BaseStat stat, float value) {
        growthStats.put(stat, value);
    }

    public float getGrowthPercStat(BaseStat stat) {
        return growthStats.get(stat);
    }

    @Override
    public Template getTemplate() {
        return new StatTemplate();
    }

    @Override
    public void fromTemplate(Template template) {
        StatTemplate statTemplate = (StatTemplate) template;
        for (RuntimeStat runtimeStat : RuntimeStat.values()) {
            this.runtimeStats.put(runtimeStat, 0.f);
        }

        for (FlagStat flagStat : FlagStat.values()) {
            this.flagStats.put(flagStat, false);
        }

        for (Map.Entry<String, Float> key : statTemplate.getValues().entrySet()) {
            String[] parts = key.getKey().split(DELIM);
            BaseStat stat = BaseStat.valueOf(parts[0]);
            switch (parts[1]) {
                case BASE:
                    baseStats.put(stat, key.getValue());
                    currentStat.put(stat, key.getValue());
                    break;
                case GROWTH:
                    growthStats.put(stat, key.getValue());
                    break;
                case GROWTH_PERCENTAGE:
                    growthPercentageStats.put(stat, key.getValue());
                    break;
            }
        }

        this.setRuntimeStat(RuntimeStat.health, getBaseStat(BaseStat.maxHealth));
        this.setRuntimeStat(RuntimeStat.resource, getBaseStat(BaseStat.maxResource));
        this.setRuntimeStat(RuntimeStat.actionPoints, getBaseStat(BaseStat.maxActionPoints));
    }

    public enum BaseStat {
        maxHealth,
        healthRegen,

        maxActionPoints,
        actionPointsRegen,

        maxResource,
        resourceRegen,

        attackDamage,
        attackRange,
        attackSpeed,

        spellPower,

        armor,
        armorPenetration,

        magicResist,
        magicResistPenetration,

        criticalStrikeChance,
        criticalStrikeDamage,

        tenacity,

        cooldownReduction,

        moveSpeed,

        cooldownAbility1,
        cooldownAbility2,
        cooldownAbility3,
        cooldownAbility4,
        cooldownPassive,
    }

    public enum RuntimeStat {
        health,
        actionPoints,
        resource,
        level,
        experience,
        attackSpeedTimer,
    }

    public enum FlagStat {
        dead, deleteOnDeath, invulnerable
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (RuntimeStat stat : RuntimeStat.values()) {
            builder.append(stat.name()).append(": ").append(String.format("%1.2f", runtimeStats.get(stat))).append("\n");
        }
        for (BaseStat stat : BaseStat.values()) {
            builder.append(stat.name()).append(": ").append(String.format("%1.2f", currentStat.get(stat))).append("\n");
        }
        for (FlagStat stat : FlagStat.values()) {
            builder.append(stat.name()).append(": ").append(flagStats.get(stat)).append("\n");
        }
        return builder.toString();
    }

    public class StatTemplate extends Template {
        private Map<String, Float> values;

        public StatTemplate() {
            this.values = new HashMap<>();
            for (BaseStat baseStat : BaseStat.values()) {
                values.put(baseStat.name() + DELIM + BASE, 0.0f);
                values.put(baseStat.name() + DELIM + GROWTH, 0.0f);
                values.put(baseStat.name() + DELIM + GROWTH_PERCENTAGE, 0.0f);
            }
        }

        public Map<String, Float> getValues() {
            return values;
        }
    }

}
