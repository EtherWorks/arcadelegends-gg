package gg.al.logic.component;

import com.artemis.PooledComponent;

/**
 * Created by Thomas Neumann on 12.06.2017.<br />
 * {@link com.artemis.Component} containing data for the {@link gg.al.logic.system.AISystem}.
 */
public class AIComponent extends PooledComponent {
    /**
     * Target of the entity.
     */
    public int target;
    /**
     * The range the entity reacts to his target.
     */
    public float aggroRange;

    @Override
    protected void reset() {
        target = 0;
        aggroRange = 0;
    }
}
