package gg.al.logic.component.data;

/**
 * Created by Thomas Neumann on 05.06.2017.<br>
 * Data class for describing heal.
 */
public class Heal {
    public float amount;
    public HealCalculationType healCalculationType;

    public Heal(float amount) {
        this(amount, HealCalculationType.Flat);
    }

    public Heal(float amount, HealCalculationType healCalculationType) {
        this.amount = amount;
        this.healCalculationType = healCalculationType;
    }

    /**
     * Enum containing the different types of calculation used for getting the total amount of heal.
     */
    public enum HealCalculationType {
        MissingHealth, MaxHealth, CurrentHealth, Flat
    }
}
