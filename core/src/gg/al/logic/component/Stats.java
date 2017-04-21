package gg.al.logic.component;

import com.artemis.PooledComponent;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Stats extends PooledComponent {

    public float maxHealth;
    public float maxActionPoints;
    public float moveSpeed;

    @Override
    protected void reset() {
        maxActionPoints = 0;
        maxHealth = 0;
        moveSpeed = 0;
    }
}
