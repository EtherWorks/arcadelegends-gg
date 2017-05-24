package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.component.data.Template;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 08.05.2017.<br />
 */
public class StatComponent extends PooledComponent implements ITemplateable {

    private final static String DELIM = "_";
    private final static String BASE = "Base";
    private final static String GROWTH = "Growth";
    private final static String GROWTH_PERCENTAGE = "GrowthPercentage";
    public final ObjectMap<String, StatusEffect> statusEffects;
    public final Array<Damage> damages;
    private final ObjectMap<FlagStat, Boolean> flags;
    private final ObjectMap<RuntimeStat, Float> runtimeStats;
    private final ObjectMap<BaseStat, Float> currentStats;
    private final ObjectMap<BaseStat, Float> baseStats;
    private final ObjectMap<BaseStat, Float> growthStats;
    private final ObjectMap<BaseStat, Float> growthPercentageStats;
    private final ObjectMap<String, Float> customStats;
    private final ObjectMap<String, Boolean> customFlags;

    public StatComponent() {
        runtimeStats = new ObjectMap<>();
        currentStats = new ObjectMap<>();
        flags = new ObjectMap<>();
        baseStats = new ObjectMap<>();
        growthStats = new ObjectMap<>();
        growthPercentageStats = new ObjectMap<>();
        statusEffects = new ObjectMap<>();
        damages = new Array<>();

        customStats = new ObjectMap<>();
        customFlags = new ObjectMap<>();
    }

    @Override
    protected void reset() {
        runtimeStats.clear();
        currentStats.clear();
        flags.clear();
        baseStats.clear();
        growthStats.clear();
        growthPercentageStats.clear();
        statusEffects.clear();
        damages.clear();
        customFlags.clear();
        customStats.clear();
    }

    public void recalculateCurrent() {
        for (BaseStat stat : BaseStat.values()) {
            float value = baseStats.get(stat);
            value += runtimeStats.get(RuntimeStat.level) * growthStats.get(stat);
            value += value * growthPercentageStats.get(stat);
            currentStats.put(stat, value);
        }
        for (StatusEffect effect : statusEffects.values()) {
            effect.applyValue(this);
        }
        for (StatusEffect effect : statusEffects.values()) {
            effect.applyPercentage(this);
        }
    }

    public void setCustomStat(String stat, float value) {
        this.customStats.put(stat, value);
    }

    public void addCustomStat(String stat, float value) {
        this.customStats.put(stat, this.customStats.get(stat) + value);
    }

    public float getCustomStat(String stat) {
        return customStats.get(stat);
    }

    public void setCustomFlag(String stat, boolean flag) {
        customFlags.put(stat, flag);
    }

    public boolean getCustomFlag(String stat) {
        return customFlags.get(stat);
    }


    public void setFlag(FlagStat stat, boolean flag) {
        flags.put(stat, flag);
    }

    public boolean getFlag(FlagStat stat) {
        return flags.get(stat);
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
        currentStats.put(stat, value);
    }

    public void addCurrentStat(BaseStat stat, float value) {
        currentStats.put(stat, currentStats.get(stat) + value);
    }

    public float getCurrentStat(BaseStat stat) {
        return currentStats.get(stat);
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
            this.flags.put(flagStat, false);
        }

        for (Map.Entry<String, Float> key : statTemplate.getValues().entrySet()) {
            String[] parts = key.getKey().split(DELIM);
            BaseStat stat = BaseStat.valueOf(parts[0]);
            switch (parts[1]) {
                case BASE:
                    baseStats.put(stat, key.getValue());
                    currentStats.put(stat, key.getValue());
                    break;
                case GROWTH:
                    growthStats.put(stat, key.getValue());
                    break;
                case GROWTH_PERCENTAGE:
                    growthPercentageStats.put(stat, key.getValue());
                    break;
            }
        }

        for (Map.Entry<String, Float> key : statTemplate.getCustomValues().entrySet()) {
            customStats.put(key.getKey(), key.getValue());
        }

        this.setRuntimeStat(RuntimeStat.health, getBaseStat(BaseStat.maxHealth));
        this.setRuntimeStat(RuntimeStat.resource, getBaseStat(BaseStat.maxResource));
        this.setRuntimeStat(RuntimeStat.actionPoints, getBaseStat(BaseStat.maxActionPoints));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (RuntimeStat stat : RuntimeStat.values()) {
            builder.append(stat.name()).append(": ").append(String.format("%1.2f", runtimeStats.get(stat))).append("\n");
        }
        for (BaseStat stat : BaseStat.values()) {
            builder.append(stat.name()).append(": ").append(String.format("%1.2f", currentStats.get(stat))).append("\n");
        }
        for (FlagStat stat : FlagStat.values()) {
            builder.append(stat.name()).append(": ").append(flags.get(stat)).append("\n");
        }
        return builder.toString();
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
        cooldownTrait,

        costAbility1,
        costAbility2,
        costAbility3,
        costAbility4,
        costTrait
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

    public class StatTemplate extends Template {
        private LinkedHashMap<String, Float> values;

        private LinkedHashMap<String, Float> customValues;

        public StatTemplate() {
            this.values = new LinkedHashMap<>();
            this.customValues = new LinkedHashMap<>();
            for (BaseStat baseStat : BaseStat.values()) {
                values.put(baseStat.name() + DELIM + BASE, 0.0f);
                values.put(baseStat.name() + DELIM + GROWTH, 0.0f);
                values.put(baseStat.name() + DELIM + GROWTH_PERCENTAGE, 0.0f);
            }
        }

        public Map<String, Float> getValues() {
            return values;
        }

        public Map<String, Float> getCustomValues() {
            return customValues;
        }
    }

}
