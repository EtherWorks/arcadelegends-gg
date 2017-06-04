package gg.al.logic.component.data;

/**
 * Created by Thomas Neumann on 28.04.2017.
 */
public class Damage {

    public DamageType damageType;
    public DamageCalculationType damageCalculationType;
    public float amount;
    public float penetration;

    public Damage(DamageType damageType, DamageCalculationType damageCalculationType, float amount, float penetration) {
        this.damageType = damageType;
        this.damageCalculationType = damageCalculationType;
        this.amount = amount;
        this.penetration = penetration;
    }

    public Damage(DamageType damageType, float amount, float penetration) {
        this(damageType, DamageCalculationType.Flat, amount, penetration);
    }

    public enum DamageType {
        True, Magic, Normal
    }

    public enum DamageCalculationType {
        MissingHealth, MaxHealth, CurrentHealth, Flat
    }
}
