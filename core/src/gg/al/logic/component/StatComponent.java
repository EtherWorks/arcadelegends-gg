package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.component.data.*;
import gg.al.util.Constants;

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
    public final Array<Heal> heals;
    private final ObjectMap<FlagStat, Boolean> flags;
    private final ObjectMap<RuntimeStat, Float> runtimeStats;
    private final ObjectMap<BaseStat, Float> currentStats;
    private final ObjectMap<BaseStat, Float> baseStats;
    private final ObjectMap<BaseStat, Float> growthStats;
    private final ObjectMap<BaseStat, Float> growthPercentageStats;
    private final ObjectMap<String, Float> customStats;
    private final ObjectMap<String, Boolean> customFlags;

    public StatEventHanlder statEventHanlder;

    public StatComponent() {
        runtimeStats = new ObjectMap<>();
        currentStats = new ObjectMap<>();
        flags = new ObjectMap<>();
        baseStats = new ObjectMap<>();
        growthStats = new ObjectMap<>();
        growthPercentageStats = new ObjectMap<>();
        statusEffects = new ObjectMap<>();
        damages = new Array<>();
        heals = new Array<>();

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
        heals.clear();
        customFlags.clear();
        customStats.clear();
        statEventHanlder = null;
    }

    public void recalculateCurrent() {
        for (int i = 0; i < BaseStat.values().length; i++) {
            BaseStat stat = BaseStat.values()[i];
            float value = baseStats.get(stat);
            value += runtimeStats.get(RuntimeStat.level) * growthStats.get(stat);
            value += value * growthPercentageStats.get(stat);
            setCurrentStat(stat, value);
        }
    }

    public void applyStatusEffects() {
        for (StatusEffect effect : statusEffects.values()) {
            effect.applyValue(this);
        }
        for (StatusEffect effect : statusEffects.values()) {
            effect.applyPercentage(this);
        }
    }


    public void setCustomStat(Enum stat, float value) {
        setCustomStat(stat.name(), value);
    }

    public void addCustomStat(Enum stat, float value) {
        addCustomStat(stat.name(), value);
    }

    public float getCustomStat(Enum stat) {
        return getCustomStat(stat.name());
    }

    public void setCustomFlag(Enum stat, boolean flag) {
        setCustomFlag(stat.name(), flag);
    }

    public boolean getCustomFlag(Enum stat) {
        return getCustomFlag(stat.name());
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
        currentStats.put(stat, value >= stat.max ? stat.max : value <= stat.min ? stat.min : value);
    }

    public void addCurrentStat(BaseStat stat, float add) {
        float value = currentStats.get(stat) + add;
        currentStats.put(stat, value >= stat.max ? stat.max : value <= stat.min ? stat.min : value);
    }

    public float getCurrentStat(BaseStat stat) {
        return currentStats.get(stat);
    }

    public void setBaseStat(BaseStat stat, float value) {
        baseStats.put(stat, value >= stat.max ? stat.max : value <= stat.min ? stat.min : value);
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
            float value = key.getValue();
            switch (parts[1]) {
                case BASE:
                    baseStats.put(stat, value >= stat.max ? stat.max : value <= stat.min ? stat.min : value);
                    currentStats.put(stat, value >= stat.max ? stat.max : value <= stat.min ? stat.min : value);
                    break;
                case GROWTH:
                    growthStats.put(stat, key.getValue());
                    break;
                case GROWTH_PERCENTAGE:
                    growthPercentageStats.put(stat, key.getValue());
                    break;
            }
        }

        for (BaseStat stat : BaseStat.values()) {
            if (!baseStats.containsKey(stat))
                baseStats.put(stat, 0f);
            if (!currentStats.containsKey(stat))
                currentStats.put(stat, 0f);
            if (!growthStats.containsKey(stat))
                growthStats.put(stat, 0f);
            if (!growthPercentageStats.containsKey(stat))
                growthPercentageStats.put(stat, 0f);
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

    public boolean shouldLevel() {
        return getRuntimeStat(RuntimeStat.experience) >= getNextLevelExperience();
    }

    public float getNextLevelExperience() {
        return (float) (100 + Math.pow(2, getRuntimeStat(RuntimeStat.level)));
    }

    public enum BaseStat {
        maxHealth(0, Float.MAX_VALUE),
        healthRegen(0, Float.MAX_VALUE),

        maxActionPoints(0, Float.MAX_VALUE),
        actionPointsRegen(0, Float.MAX_VALUE),

        maxResource(0, Float.MAX_VALUE),
        resourceRegen(0, Float.MAX_VALUE),

        attackDamage(0, Float.MAX_VALUE),
        attackRange(0, Float.MAX_VALUE),
        attackSpeed(0, Constants.MAX_ATTACKSPEED),

        spellPower(0, Float.MAX_VALUE),

        armor(0, Float.MAX_VALUE),
        armorPenetration(0, Float.MAX_VALUE),

        magicResist(0, Float.MAX_VALUE),
        magicResistPenetration(0, Float.MAX_VALUE),

        damageAmplification(0, Float.MAX_VALUE),
        healAmplification(0, Float.MAX_VALUE),
        damageReduction(0, Float.MAX_VALUE),
        healReduction(0, Float.MAX_VALUE),

        criticalStrikeChance(0, Float.MAX_VALUE),
        criticalStrikeDamage(0, Float.MAX_VALUE),

        tenacity(0, Float.MAX_VALUE),

        cooldownReduction(0, Constants.MAX_CDR),

        moveSpeed(0, Float.MAX_VALUE),

        cooldownAbility1(0, Float.MAX_VALUE),
        cooldownAbility2(0, Float.MAX_VALUE),
        cooldownAbility3(0, Float.MAX_VALUE),
        cooldownAbility4(0, Float.MAX_VALUE),
        cooldownTrait(0, Float.MAX_VALUE),

        costAbility1(0, Float.MAX_VALUE),
        costAbility2(0, Float.MAX_VALUE),
        costAbility3(0, Float.MAX_VALUE),
        costAbility4(0, Float.MAX_VALUE),
        costTrait(0, Float.MAX_VALUE),

        castTimeAbility1(0, Float.MAX_VALUE),
        castTimeAbility2(0, Float.MAX_VALUE),
        castTimeAbility3(0, Float.MAX_VALUE),
        castTimeAbility4(0, Float.MAX_VALUE),
        castTimeTrait(0, Float.MAX_VALUE),
        attackPrepTime(0, Float.MAX_VALUE);

        public final float min;
        public final float max;

        BaseStat(float min, float max) {
            this.max = max;
            this.min = min;
        }
    }

    public enum RuntimeStat {
        health,
        actionPoints,
        resource,
        level,
        experience,
        skillPoints,
        ability_1_points,
        ability_2_points,
        ability_3_points,
        ability_4_points,
        trait_points
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

    public interface StatEventHanlder
    {
        void onDeath(StatComponent statComponent, int entityId);
    }
}
