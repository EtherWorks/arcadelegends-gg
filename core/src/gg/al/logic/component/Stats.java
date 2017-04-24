package gg.al.logic.component;

import com.artemis.PooledComponent;
import gg.al.logic.entity.EntityArguments;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Stats extends PooledComponent {

    public enum Fields {
        maxHealth,
        healthRegen,
        maxActionPoints,
        actionPointRegen,
        maxResource,
        resourceRegen,
        attackDamage,
        abilityPower,
        armor,
        magicResist,
        criticalStrikeChance,
        cooldownReduction,
        moveSpeed
    }

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
    public float abilityPower;

    public float armor;
    public float magicResist;

    public float criticalStrikeChance;

    public float cooldownReduction;

    public float moveSpeed;

    public float level;
    public float experience;

    public void set(EntityArguments arguments) {
        maxHealth = arguments.get(Fields.maxHealth.toString(), Integer.class);
        healthRegen = arguments.get(Fields.healthRegen.toString(), Float.class);
        health = maxHealth;

        maxActionPoints = arguments.get(Fields.maxActionPoints.toString(), Integer.class);
        actionPointRegen = arguments.get(Fields.actionPointRegen.toString(), Float.class);
        actionPoints = maxActionPoints;

        maxResource = arguments.get(Fields.maxResource.toString(), Integer.class);
        resourceRegen = arguments.get(Fields.resourceRegen.toString(), Float.class);
        resource = maxResource;

        attackDamage = arguments.get(Fields.attackDamage.toString(), Float.class);
        abilityPower = arguments.get(Fields.abilityPower.toString(), Float.class);

        armor = arguments.get(Fields.armor.toString(), Float.class);
        magicResist = arguments.get(Fields.magicResist.toString(), Float.class);

        criticalStrikeChance = arguments.get(Fields.criticalStrikeChance.toString(), Float.class);

        cooldownReduction = arguments.get(Fields.cooldownReduction.toString(), Float.class);

        moveSpeed = arguments.get(Fields.moveSpeed.toString(), Float.class);
        level = 1;
        experience = 0;
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
        abilityPower = 0;
        armor = 0;
        magicResist = 0;
        criticalStrikeChance = 0;
        cooldownReduction = 0;
    }
}
