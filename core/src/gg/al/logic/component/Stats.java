package gg.al.logic.component;

import gg.al.logic.entity.EntityArguments;

import java.util.Map;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Stats extends PooledDefComponent {

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

    public float tenacity;

    public float cooldownReduction;

    public float moveSpeed;

    public int level;
    public float experience;

    public boolean dead;
    public boolean invulnerable;

    public void set(EntityArguments arguments) {
        Map<String, Object> stats = arguments.get("Stats", Map.class);
        maxHealth = (int) (double) stats.get("maxHealth");
        healthRegen = (float) (double) stats.get("healthRegen");
        health = maxHealth;

        maxActionPoints = (int) (double) stats.get("maxActionPoints");
        actionPointRegen = (float) (double) stats.get("actionPointRegen");
        actionPoints = maxActionPoints;

        maxResource = (int) (double) stats.get("maxResource");
        resourceRegen = (float) (double) stats.get("resourceRegen");
        resource = maxResource;

        attackDamage = (float) (double) stats.get("attackDamage");
        spellPower = (float) (double) stats.get("spellPower");

        armor = (float) (double) stats.get("armor");
        magicResist = (float) (double) stats.get("magicResist");

        criticalStrikeChance = (float) (double) stats.get("criticalStrikeChance");

        cooldownReduction = (float) (double) stats.get("cooldownReduction");

        moveSpeed = (float) (double) stats.get("moveSpeed");

        attackSpeed = (float) (double) stats.get("attackSpeed");
        armorPenetration = (float) (double) stats.get("armorPenetration");
        magicResistPenetration = (float) (double) stats.get("magicResistPenetration");
        criticalStrikeDamage = (float) (double) stats.get("criticalStrikeDamage");
        tenacity = (float) (double) stats.get("tenacity");
        dead = false;
        invulnerable = false;
    }

    @Override
    public IComponentDef getDefaultDef() {
        return new StatsDef();
    }

    @Override
    public void fromDef(IComponentDef def) {
        StatsDef stats = (StatsDef) def;
        maxHealth = stats.maxHealth;
        healthRegen = stats.healthRegen;

        maxActionPoints = stats.maxActionPoints;
        actionPointRegen = stats.actionPointRegen;

        maxResource = stats.maxResource;
        resourceRegen = stats.resourceRegen;

        attackDamage = stats.attackDamage;
        spellPower = stats.spellPower;

        armor = stats.armor;
        magicResist = stats.magicResist;

        criticalStrikeChance = stats.criticalStrikeChance;

        cooldownReduction = stats.cooldownReduction;

        moveSpeed = stats.moveSpeed;

        attackSpeed = stats.attackSpeed;
        armorPenetration = stats.armorPenetration;
        magicResistPenetration = stats.magicResistPenetration;
        criticalStrikeDamage = stats.criticalStrikeDamage;
        tenacity = stats.tenacity;
        dead = false;
        invulnerable = false;
    }

    @Override
    public String toString() {
        return health + " / " + maxHealth + " HP, " + healthRegen + " HP/s" +
                "\n" + actionPoints + " / " + maxActionPoints + " AP, " + actionPointRegen + " AP/s" +
                "\n" + resource + " / " + maxResource + " R, " + resourceRegen + " R/s" +
                "\n" + attackDamage + " AD" +
                "\n" + attackSpeed + " AS" +
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
    }

    public static class StatsDef extends IComponentDef {
        public int maxHealth;
        public float healthRegen;

        public int maxActionPoints;
        public float actionPointRegen;

        public int maxResource;
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

        public float tenacity;

        public float cooldownReduction;

        public float moveSpeed;
    }
}
