package gg.al.logic.component;

import com.artemis.PooledComponent;
import gg.al.logic.data.StatusEffect;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 */
public class StatusEffects extends PooledComponent {

    public final Map<String, StatusEffect> statusEffects;

    public StatusEffects() {
        statusEffects = new LinkedHashMap<>();
    }

    @Override
    protected void reset() {
        statusEffects.clear();
    }
}
