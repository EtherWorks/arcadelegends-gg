package gg.al.logic.component;

import com.artemis.PooledComponent;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Stats extends PooledComponent {

    public int maxHealth;
    public int maxActionPoints;
    public float moveSpeed;
    public float actionPointRegen;

    public float actionPoints;

    @Override
    protected void reset() {
        maxActionPoints = 0;
        maxHealth = 0;
        moveSpeed = 0;
        actionPointRegen = 0;
        actionPoints = 0;
    }
}
