package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.ObjectMap;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.CharacterComponent;
import gg.al.logic.component.InventoryComponent;
import gg.al.logic.component.StatComponent;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.Heal;
import gg.al.logic.component.data.Item;
import gg.al.logic.component.data.StatusEffect;

/**
 * Created by Thomas Neumann on 24.04.2017.<br />
 * {@link IteratingSystem} responsible for updating the current stats of the {@link StatComponent}.<br>
 * Contains every necessary calculation from {@link Item} to {@link StatusEffect}.<br>
 * Also controls the death of entities.
 */
public class StatSystem extends IteratingSystem {

    private ComponentMapper<StatComponent> mapperStatComponent;
    private ComponentMapper<CharacterComponent> mapperCharacterComponent;
    private ComponentMapper<InventoryComponent> mapperInventoryComponent;

    private ArcadeWorld arcadeWorld;

    public StatSystem(ArcadeWorld arcadeWorld) {
        super(Aspect.all(StatComponent.class));
        this.arcadeWorld = arcadeWorld;
    }

    @Override
    protected void process(int entityId) {
        StatComponent stats = mapperStatComponent.get(entityId);
        CharacterComponent characterComponent = mapperCharacterComponent.get(entityId);

        /**
         * Check for death.
         */
        if (stats.getFlag(StatComponent.FlagStat.dead)) {
            if (stats.statEventHandler != null) {
                stats.statEventHandler.onDeath(stats, entityId);
                stats.statEventHandler = null;
            }
            if (stats.getFlag(StatComponent.FlagStat.deleteOnDeath)) {
                arcadeWorld.delete(entityId);
            }
            return;
        }

        /**
         * Potentially level up
         */
        if (stats.shouldLevel()) {
            stats.addRuntimeStat(StatComponent.RuntimeStat.experience, -stats.getNextLevelExperience());
            stats.addRuntimeStat(StatComponent.RuntimeStat.level, 1);
            stats.addRuntimeStat(StatComponent.RuntimeStat.skillPoints, 1);
        }

        /**
         * Tick the {@link StatusEffect} down.
         */
        for (ObjectMap.Values<StatusEffect> values = stats.statusEffects.values();
             values.hasNext(); ) {
            StatusEffect effect = values.next();
            if (effect.effectTime == 0) {
                if (effect.tickHandler != null)
                    effect.tickHandler.onTick(getWorld().getDelta(), stats);
                continue;
            }
            if (effect.remainingTime == -1)
                effect.remainingTime = effect.effectTime;

            if (effect.remainingTime - getWorld().getDelta() <= 0) {
                values.remove();
            } else {
                effect.remainingTime -= getWorld().getDelta();
                if (effect.tickHandler != null)
                    effect.tickHandler.onTick(getWorld().getDelta(), stats);
            }
        }

        /**
         * Recalculate from base.
         */
        stats.recalculateCurrent();

        /**
         * Apply {@link Item} changes.
         */
        InventoryComponent inventoryComponent = mapperInventoryComponent.get(entityId);
        if (inventoryComponent != null) {
            for (int i = 0; i < inventoryComponent.items.length; i++) {
                Item item = inventoryComponent.items[i];
                if (item != null)
                    item.applyValue(stats);
            }
            for (int i = 0; i < inventoryComponent.items.length; i++) {
                Item item = inventoryComponent.items[i];
                if (item != null)
                    item.applyPercentage(stats);
            }
        }
        /**
         * Apply {@link StatusEffect} changes.
         */
        stats.applyStatusEffects();
        /**
         * Apply {@link Character} changes.
         */
        if (characterComponent != null) {
            characterComponent.character.affectStats(stats);
        }

        /**
         * Apply the queued {@link Heal}.
         */
        for (Heal heal : stats.heals) {
            float amount = 0;
            switch (heal.healCalculationType) {
                case Flat:
                    amount = heal.amount;
                    break;
                case MaxHealth:
                    amount = stats.getCurrentStat(StatComponent.BaseStat.maxHealth) * heal.amount;
                    break;
                case CurrentHealth:
                    amount = stats.getRuntimeStat(StatComponent.RuntimeStat.health) * heal.amount;
                    break;
                case MissingHealth:
                    amount = (stats.getCurrentStat(StatComponent.BaseStat.maxHealth) - stats.getRuntimeStat(StatComponent.RuntimeStat.health)) * heal.amount;
                    break;
            }
            amount += amount * stats.getCurrentStat(StatComponent.BaseStat.healAmplification);
            amount -= amount * stats.getCurrentStat(StatComponent.BaseStat.healReduction);
            float health = stats.getRuntimeStat(StatComponent.RuntimeStat.health);
            float maxHealth = stats.getCurrentStat(StatComponent.BaseStat.maxHealth);
            stats.setRuntimeStat(StatComponent.RuntimeStat.health, health + amount >= maxHealth ? maxHealth : health + amount);
            stats.heals.removeValue(heal, true);
        }

        /**
         * Apply the queued {@link Damage}.
         */
        if (!stats.getFlag(StatComponent.FlagStat.invulnerable)) {
            for (Damage damage : stats.damages) {
                float amount = 0;
                switch (damage.damageCalculationType) {
                    case Flat:
                        amount = damage.amount;
                        break;
                    case MaxHealth:
                        amount = stats.getCurrentStat(StatComponent.BaseStat.maxHealth) * damage.amount;
                        break;
                    case CurrentHealth:
                        amount = stats.getRuntimeStat(StatComponent.RuntimeStat.health) * damage.amount;
                        break;
                    case MissingHealth:
                        amount = (stats.getCurrentStat(StatComponent.BaseStat.maxHealth) - stats.getRuntimeStat(StatComponent.RuntimeStat.health)) * damage.amount;
                        break;
                }

                amount += amount * stats.getCurrentStat(StatComponent.BaseStat.damageAmplification);
                amount -= amount * stats.getCurrentStat(StatComponent.BaseStat.damageReduction);

                switch (damage.damageType) {
                    case True:
                        break;
                    case Magic:
                        float mr = stats.getCurrentStat(StatComponent.BaseStat.magicResist) - stats.getCurrentStat(StatComponent.BaseStat.magicResist) * damage.penetration / 100;
                        amount *= 100 / (100 + mr);
                        break;
                    case Physical:
                        float ar = stats.getCurrentStat(StatComponent.BaseStat.armor) - stats.getCurrentStat(StatComponent.BaseStat.armor) * damage.penetration / 100;
                        amount *= 100 / (100 + ar);
                        break;
                }
                if ((int) (stats.getRuntimeStat(StatComponent.RuntimeStat.health) - amount) <= 0) {
                    stats.setRuntimeStat(StatComponent.RuntimeStat.health, 0);
                    stats.setRuntimeStat(StatComponent.RuntimeStat.resource, 0);
                    stats.setRuntimeStat(StatComponent.RuntimeStat.actionPoints, 0);
                    stats.setFlag(StatComponent.FlagStat.dead, true);
                } else
                    stats.addRuntimeStat(StatComponent.RuntimeStat.health, -amount);

                stats.damages.removeValue(damage, true);
            }
        }
    }
}
