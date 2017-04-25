package gg.al.logic.component;

import com.artemis.PooledComponent;

/**
 * Created by Thomas Neumann on 25.04.2017.<br />
 */
public class Damage extends PooledComponent {

    public DamageType damageType;
    public float amount;
    public float penetration;

    @Override
    protected void reset() {
        damageType = DamageType.Normal;

    }

    public enum DamageType {
        True, Magic, Normal
    }
}
