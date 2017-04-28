package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.component.StatusEffects;
import gg.al.logic.data.StatusEffect;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 */
@Slf4j
public class StatusSystem extends IteratingSystem {

    private ComponentMapper<StatusEffects> mapperStatusEffects;

    public StatusSystem() {
        super(Aspect.all(StatusEffects.class));
    }

    @Override
    protected void process(int entityId) {
        StatusEffects statusEffects = mapperStatusEffects.get(entityId);
        for (ObjectMap.Values<StatusEffect> values = statusEffects.statusEffects.values();
             values.hasNext(); ) {
            StatusEffect effect = values.next();
            if (effect.effectTime == 0)
                continue;
            if (effect.remainingTime == -1)
                effect.remainingTime = effect.effectTime;

            if (effect.remainingTime - getWorld().getDelta() <= 0) {
                values.remove();
            } else {
                effect.remainingTime -= getWorld().getDelta();
            }
        }
    }
}