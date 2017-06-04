package gg.al.logic.component.data;

/**
 * Created by Thomas Neumann on 05.06.2017.
 */
public class Heal {
    public float amount;
    public HealType healType;

    public Heal(float amount) {
        this(amount, HealType.Flat);
    }

    public Heal(float amount, HealType healType) {
        this.amount = amount;
        this.healType = healType;
    }

    public enum HealType {
        MissingHealth, MaxHealth, CurrentHealth, Flat
    }
}
