package gg.al.logic.component;

import com.artemis.PooledComponent;

/**
 * Created by Thomas Neumann on 12.06.2017.<br />
 */
public class AIComponent extends PooledComponent {
    public int target;
    public float aggroRange;

    @Override
    protected void reset() {
        target = 0;
        aggroRange = 0;
    }
}
