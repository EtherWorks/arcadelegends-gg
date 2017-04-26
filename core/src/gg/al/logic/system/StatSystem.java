package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.Stats;
import gg.al.logic.component.StatusEffects;
import gg.al.logic.data.StatusEffect;

/**
 * Created by Thomas Neumann on 24.04.2017.<br />
 */
public class StatSystem extends IteratingSystem {

    private ComponentMapper<Stats> mapperStats;
    private ComponentMapper<StatusEffects> mapperStatusEffects;

    public StatSystem() {
        super(Aspect.all(Stats.class));
    }

    private static void applyLevel(Stats stats) {
        stats.maxHealth += (int) calculateStatGrowth(stats.baseStats.baseHealth, stats.level, stats.baseStats.healthGrowth);
        stats.healthRegen += calculateStatGrowth(stats.baseStats.baseHealthRegen, stats.level, stats.baseStats.healthRegenGrowth);

        stats.maxActionPoints += (int) calculateStatGrowth(stats.baseStats.baseActionPoints, stats.level, stats.baseStats.actionPointsGrowth);
        stats.actionPointRegen += calculateStatGrowth(stats.baseStats.baseActionPointsRegen, stats.level, stats.baseStats.actionPointsRegenGrowth);

        stats.maxResource += (int) calculateStatGrowth(stats.baseStats.baseResource, stats.level, stats.baseStats.resourceGrowth);
        stats.resourceRegen += calculateStatGrowth(stats.baseStats.baseResourceRegen, stats.level, stats.baseStats.resourceRegenGrowth);

        stats.attackDamage += calculateStatGrowth(stats.baseStats.baseAttackDamage, stats.level, stats.baseStats.attackDamageGrowth);
        stats.spellPower += calculateStatGrowth(stats.baseStats.baseSpellPower, stats.level, stats.baseStats.spellPowerGrowth);

        stats.armor += calculateStatGrowth(stats.baseStats.baseArmor, stats.level, stats.baseStats.armorGrowth);
        stats.magicResist += calculateStatGrowth(stats.baseStats.baseMagicResist, stats.level, stats.baseStats.magicResistGrowth);

        stats.criticalStrikeChance += calculateStatGrowth(stats.baseStats.baseCriticalStrikeChance, stats.level, stats.baseStats.criticalStrikeChanceGrowth);

        stats.cooldownReduction += calculateStatGrowth(stats.baseStats.baseCooldownReduction, stats.level, stats.baseStats.cooldownReductionGrowth);

        stats.moveSpeed += calculateStatGrowth(stats.baseStats.baseMoveSpeed, stats.level, stats.baseStats.moveSpeedGrowth);

        stats.attackSpeed += calculateStatGrowth(stats.baseStats.baseAttackSpeed, stats.level, stats.baseStats.attackSpeedGrowth);
        stats.armorPenetration += calculateStatGrowth(stats.baseStats.baseArmorPenetration, stats.level, stats.baseStats.armorPenetrationGrowth);
        stats.magicResistPenetration += calculateStatGrowth(stats.baseStats.baseMagicResistPenetration, stats.level, stats.baseStats.magicResistPenetrationGrowth);
        stats.criticalStrikeDamage += calculateStatGrowth(stats.baseStats.baseCriticalStrikeDamage, stats.level, stats.baseStats.criticalStrikeDamageGrowth);
    }

    public static float calculateStatGrowth(float stat, int level, float growth) {
        return level * growth;
    }

    private static void resetToBase(Stats stats) {
        stats.maxHealth = stats.baseStats.baseHealth;
        stats.healthRegen = stats.baseStats.baseHealthRegen;

        stats.maxActionPoints = stats.baseStats.baseActionPoints;
        stats.actionPointRegen = stats.baseStats.baseActionPointsRegen;

        stats.maxResource = stats.baseStats.baseResource;
        stats.resourceRegen = stats.baseStats.baseResourceRegen;

        stats.attackDamage = stats.baseStats.baseAttackDamage;
        stats.spellPower = stats.baseStats.baseSpellPower;

        stats.armor = stats.baseStats.baseArmor;
        stats.magicResist = stats.baseStats.baseMagicResist;

        stats.criticalStrikeChance = stats.baseStats.baseCriticalStrikeChance;

        stats.cooldownReduction = stats.baseStats.baseCooldownReduction;

        stats.moveSpeed = stats.baseStats.baseMoveSpeed;

        stats.attackSpeed = stats.baseStats.baseAttackSpeed;
        stats.armorPenetration = stats.baseStats.baseArmorPenetration;
        stats.magicResistPenetration = stats.baseStats.baseMagicResistPenetration;
        stats.criticalStrikeDamage = stats.baseStats.baseCriticalStrikeDamage;
    }

    @Override
    protected void process(int entityId) {
        Stats stats = mapperStats.get(entityId);
        if (stats.dead)
            return;

        resetToBase(stats);
        applyLevel(stats);

        StatusEffects statusEffects = mapperStatusEffects.get(entityId);

        if (statusEffects != null) {
            for (StatusEffect effect : statusEffects.statusEffects.values()) {
                effect.applyValue(stats);
            }
            for (StatusEffect effect : statusEffects.statusEffects.values()) {
                effect.applyPercentage(stats);
            }
        }
    }
}
