package gg.al.logic.data;

import gg.al.logic.component.Stats;
import lombok.Builder;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 */
@Builder
public class StatusEffect {

    public float effectTime;
    @Builder.Default
    public float remainingTime = -1;

    public float percentageMaxHealth;
    public float percentageHealthRegen;

    public int percentageMaxActionPoints;
    public float percentageActionPointsRegen;

    public int percentageMaxResource;
    public float percentageResourceRegen;

    public float percentageAttackDamage;
    public float percentageAttackSpeed;

    public float percentageSpellPower;

    public float percentageArmor;
    public float percentageArmorPenetration;

    public float percentageMagicResist;
    public float percentageMagicResistPenetration;

    public float percentageCriticalStrikeChance;
    public float percentageCriticalStrikeDamage;

    public float percentageTenacity;

    public float percentageCooldownReduction;

    public float percentageMoveSpeed;

    public int valueMaxHealth;
    public float valueHealthRegen;

    public int valueMaxActionPoints;
    public float valueActionPointsRegen;

    public int valueMaxResource;
    public float valueResourceRegen;

    public float valueAttackDamage;
    public float valueAttackSpeed;

    public float valueSpellPower;

    public float valueArmor;
    public float valueArmorPenetration;

    public float valueMagicResist;
    public float valueMagicResistPenetration;

    public float valueCriticalStrikeChance;
    public float valueCriticalStrikeDamage;

    public float valueTenacity;

    public float valueCooldownReduction;

    public float valueMoveSpeed;


    public void apply(Stats stats) {
        applyValue(stats);
        applyPercentage(stats);
    }

    private void applyValue(Stats stats) {
        stats.maxHealth += valueMaxHealth;
        stats.healthRegen += valueHealthRegen;

        stats.maxActionPoints += valueMaxActionPoints;
        stats.actionPointRegen += valueActionPointsRegen;

        stats.maxResource += valueMaxResource;
        stats.resourceRegen += valueResourceRegen;

        stats.attackDamage += valueAttackDamage;
        stats.spellPower += valueSpellPower;

        stats.armor += valueArmor;
        stats.magicResist += valueMagicResist;

        stats.criticalStrikeChance += valueCriticalStrikeChance;

        stats.cooldownReduction += valueCooldownReduction;

        stats.moveSpeed += valueMoveSpeed;

        stats.attackSpeed += valueAttackSpeed;
        stats.armorPenetration += valueArmorPenetration;
        stats.magicResistPenetration += valueMagicResistPenetration;
        stats.criticalStrikeDamage += valueCriticalStrikeDamage;
    }

    private void applyPercentage(Stats stats) {
        stats.maxHealth += stats.maxHealth * percentageMaxHealth;
        stats.healthRegen += stats.healthRegen * percentageHealthRegen;

        stats.maxActionPoints += stats.maxActionPoints * percentageMaxActionPoints;
        stats.actionPointRegen += stats.actionPointRegen * percentageActionPointsRegen;

        stats.maxResource += stats.maxResource * percentageMaxResource;
        stats.resourceRegen += stats.resourceRegen * percentageResourceRegen;

        stats.attackDamage += stats.attackDamage * percentageAttackDamage;
        stats.spellPower += stats.spellPower * percentageSpellPower;

        stats.armor += stats.armor * percentageArmor;
        stats.magicResist += stats.magicResist * percentageMagicResist;

        stats.criticalStrikeChance += stats.criticalStrikeChance * percentageCriticalStrikeChance;

        stats.cooldownReduction += stats.cooldownReduction * percentageCooldownReduction;

        stats.moveSpeed += stats.moveSpeed * percentageMoveSpeed;

        stats.attackSpeed += stats.attackSpeed * percentageAttackSpeed;
        stats.armorPenetration += stats.armorPenetration * percentageArmorPenetration;
        stats.magicResistPenetration += stats.magicResistPenetration * percentageMagicResistPenetration;
        stats.criticalStrikeDamage += stats.criticalStrikeDamage * percentageCriticalStrikeDamage;
    }


}
