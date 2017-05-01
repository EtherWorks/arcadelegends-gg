package gg.al.logic.data;

/**
 * Created by Thomas Neumann on 28.04.2017.
 */
public class Damage {

    public DamageType damageType;
    public float amount;
    public float penetration;

    public Damage(DamageType damageType, float amount, float penetration) {
        this.damageType = damageType;
        this.amount = amount;
        this.penetration = penetration;
    }

    public enum DamageType {
        True, Magic, Normal
    }
}
