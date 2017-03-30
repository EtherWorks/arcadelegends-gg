package gg.al.logic.component;

import com.artemis.PooledComponent;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Stats extends PooledComponent {

    public float maxHealth;
    public float maxActionPoints;

    @Override
    protected void reset() {
        maxActionPoints = 0;
        maxHealth = 0;
    }

    public void set(float maxHealth, float maxActionPoints) {
        this.maxHealth = maxHealth;
        this.maxActionPoints = maxActionPoints;
    }
}
