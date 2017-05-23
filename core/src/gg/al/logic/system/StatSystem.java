package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.character.Character;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.StatusEffect;

/**
 * Created by Thomas Neumann on 24.04.2017.<br />
 */
public class StatSystem extends IteratingSystem {

    private ComponentMapper<StatComponent> mapperStatComponent;
    private ComponentMapper<CharacterComponent> mapperCharacterComponent;

    private ArcadeWorld arcadeWorld;

    public StatSystem(ArcadeWorld arcadeWorld) {
        super(Aspect.all(StatComponent.class));
        this.arcadeWorld = arcadeWorld;
    }

    @Override
    protected void process(int entityId) {
        StatComponent stats = mapperStatComponent.get(entityId);
        CharacterComponent characterComponent = mapperCharacterComponent.get(entityId);

        if (stats.getFlagStat(StatComponent.FlagStat.dead)) {
            if (stats.getFlagStat(StatComponent.FlagStat.deleteOnDeath)) {
                arcadeWorld.delete(entityId);
            }
            return;
        }

        for (ObjectMap.Values<StatusEffect> values = stats.statusEffects.values();
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

        stats.recalculateCurrent();
        if (characterComponent != null) {
            characterComponent.character.affectStats(stats);
        }

        if (!stats.getFlagStat(StatComponent.FlagStat.dead) &&
                !stats.getFlagStat(StatComponent.FlagStat.invulnerable)) {

            for (Damage damage : stats.damages) {
                float amount = damage.amount;
                switch (damage.damageType) {
                    case True:
                        break;
                    case Magic:
                        float mr = stats.getCurrentStat(StatComponent.BaseStat.magicResist) - stats.getCurrentStat(StatComponent.BaseStat.magicResist) * damage.penetration / 100;
                        amount = amount - amount * mr / 100;
                        break;
                    case Normal:
                        float ar = stats.getCurrentStat(StatComponent.BaseStat.armor) - stats.getCurrentStat(StatComponent.BaseStat.armor) * damage.penetration / 100;
                        amount = amount - amount * ar / 100;
                        break;
                }
                if ((int) (stats.getRuntimeStat(StatComponent.RuntimeStat.health) - amount) <= 0) {
                    stats.setRuntimeStat(StatComponent.RuntimeStat.health, 0);
                    stats.setRuntimeStat(StatComponent.RuntimeStat.resource, 0);
                    stats.setRuntimeStat(StatComponent.RuntimeStat.actionPoints, 0);
                    stats.setFlagStat(StatComponent.FlagStat.dead, true);
                } else
                    stats.addRuntimeStat(StatComponent.RuntimeStat.health, -amount);
                stats.damages.removeValue(damage, true);
            }
        }
    }
}
