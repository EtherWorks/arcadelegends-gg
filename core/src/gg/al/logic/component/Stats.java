package gg.al.logic.component;

import gg.al.logic.data.IComponentDef;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Stats extends PooledDefComponent {

    public StatsDef baseStats;

    public int maxHealth;
    public float healthRegen;
    public float health;

    public int maxActionPoints;
    public float actionPointRegen;
    public float actionPoints;

    public int maxResource;
    public float resource;
    public float resourceRegen;

    public float attackDamage;
    public float attackSpeed;
    public float spellPower;

    public float armor;
    public float armorPenetration;
    public float magicResist;
    public float magicResistPenetration;

    public float criticalStrikeChance;
    public float criticalStrikeDamage;

    public float attackRange;

    public float tenacity;

    public float cooldownReduction;

    public float moveSpeed;

    public int level;
    public float experience;

    public float attackSpeedTimer;

    public boolean dead;
    public boolean deleteOnDeath;
    public boolean invulnerable;

    @Override
    public void fromDef(IComponentDef def) {
        StatsDef statsDef = (StatsDef) def;
        this.baseStats = statsDef;
        maxHealth = baseStats.baseHealth;
        healthRegen = baseStats.baseHealthRegen;
        health = maxHealth;

        maxActionPoints = baseStats.baseActionPoints;
        actionPointRegen = baseStats.baseActionPointsRegen;
        actionPoints = maxActionPoints;

        maxResource = baseStats.baseResource;
        resourceRegen = baseStats.baseResourceRegen;
        resource = maxResource;

        attackDamage = baseStats.baseAttackDamage;
        attackRange = baseStats.baseAttackRange;
        spellPower = baseStats.baseSpellPower;

        armor = baseStats.baseArmor;
        magicResist = baseStats.baseMagicResist;

        criticalStrikeChance = baseStats.baseCriticalStrikeChance;

        cooldownReduction = baseStats.baseCooldownReduction;

        moveSpeed = baseStats.baseMoveSpeed;

        attackSpeed = baseStats.baseAttackSpeed;
        attackSpeedTimer = 0;

        armorPenetration = baseStats.baseArmorPenetration;
        magicResistPenetration = baseStats.baseMagicResistPenetration;
        criticalStrikeDamage = baseStats.baseCriticalStrikeDamage;
        tenacity = baseStats.baseTenacity;
        dead = false;
        deleteOnDeath = false;
        invulnerable = false;
    }

    @Override
    public IComponentDef getDefaultDef() {
        return new StatsDef();
    }

    @Override
    public String toString() {
        return health + " / " + maxHealth + " HP, " + healthRegen + " HP/s" +
                "\n" + actionPoints + " / " + maxActionPoints + " AP, " + actionPointRegen + " AP/s" +
                "\n" + resource + " / " + maxResource + " R, " + resourceRegen + " R/s" +
                "\n" + attackDamage + " AD" +
                "\n" + attackRange + " RA" +
                "\n" + attackSpeed + " AS" +
                "\n" + attackSpeedTimer + " ASt" +
                "\n" + spellPower + " SP" +
                "\n" + armor + " AR" +
                "\n" + armorPenetration + " ARPEN" +
                "\n" + magicResist + " MR" +
                "\n" + magicResistPenetration + " MRPEN" +
                "\n" + criticalStrikeChance + " CRIT" +
                "\n" + criticalStrikeDamage + " CRITDMG" +
                "\n" + tenacity + " TEN" +
                "\n" + cooldownReduction + " CDR" +
                "\n" + moveSpeed + " MS" +
                "\n" + level + " LVL" +
                "\n" + experience + " XP" +
                "\n" + dead + " DEAD";
    }

    @Override
    protected void reset() {
        maxActionPoints = 0;
        maxHealth = 0;
        moveSpeed = 0;
        actionPointRegen = 0;
        actionPoints = 0;
        health = 0;
        healthRegen = 0;
        maxResource = 0;
        resource = 0;
        resourceRegen = 0;
        attackDamage = 0;
        spellPower = 0;
        armor = 0;
        magicResist = 0;
        criticalStrikeChance = 0;
        cooldownReduction = 0;
        attackSpeed = 0;
        armorPenetration = 0;
        magicResistPenetration = 0;
        criticalStrikeDamage = 0;
        tenacity = 0;
        attackRange = 0;
        dead = false;
        invulnerable = false;
        deleteOnDeath = false;
        baseStats = null;
    }

    public static class StatsDef extends IComponentDef {

        public int baseHealth;
        public float baseHealthRegen;

        public int baseActionPoints;
        public float baseActionPointsRegen;

        public int baseResource;
        public float baseResourceRegen;

        public float baseAttackDamage;
        public float baseAttackRange;
        public float baseAttackSpeed;

        public float baseSpellPower;

        public float baseArmor;
        public float baseArmorPenetration;

        public float baseMagicResist;
        public float baseMagicResistPenetration;

        public float baseCriticalStrikeChance;
        public float baseCriticalStrikeDamage;

        public float baseTenacity;

        public float baseCooldownReduction;

        public float baseMoveSpeed;

        public int healthGrowth;
        public float healthRegenGrowth;

        public int actionPointsGrowth;
        public float actionPointsRegenGrowth;

        public int resourceGrowth;
        public float resourceRegenGrowth;

        public float attackDamageGrowth;
        public float attackRangeGrowth;
        public float attackSpeedGrowth;

        public float spellPowerGrowth;

        public float armorGrowth;
        public float armorPenetrationGrowth;

        public float magicResistGrowth;
        public float magicResistPenetrationGrowth;

        public float criticalStrikeChanceGrowth;
        public float criticalStrikeDamageGrowth;

        public float tenacityGrowth;

        public float cooldownReductionGrowth;

        public float moveSpeedGrowth;
    }
}
