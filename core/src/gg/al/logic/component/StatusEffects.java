package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.data.StatusEffect;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 */
public class StatusEffects extends PooledComponent {

    public final ObjectMap<String, StatusEffect> statusEffects;

    public StatusEffects() {
        statusEffects = new ObjectMap<>();
    }

    @Override
    protected void reset() {
        statusEffects.clear();
    }
}
