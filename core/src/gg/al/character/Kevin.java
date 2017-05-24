package gg.al.character;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import gg.al.logic.component.ControlComponent;
import gg.al.logic.component.RenderComponent;
import gg.al.logic.component.StatComponent;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.StatusEffect;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
public class Kevin extends Character {

    public float ABILITY_2_RANGE = 1.5f;
    public float ABILITY_4_BOOST = .6f;
    public String BLEED_NAME = "KevBleed";
    public String BOOST_NAME = "KevBoost";

    private StatusEffect.StatusEffectBuilder boost = StatusEffect.builder()
            .effectTime(0)
            .percentageStat(StatComponent.BaseStat.attackSpeed, ABILITY_4_BOOST)
            .percentageStat(StatComponent.BaseStat.armor, -.5f);
    private StatusEffect.StatusEffectBuilder bleed = StatusEffect.builder()
            .effectTime(5);
    private boolean ability4_activate = false;

    @Override
    protected void onTick(float delta) {

    }

    @Override
    protected void onCast(int abilityInd) {

        StatComponent casterStat = getComponent(entityID, StatComponent.class);
        switch (abilityInd) {
            case ABILITY_1:
                RenderComponent renderComponent = getComponent(entityID, RenderComponent.class);
                Vector2 start = vectorPool.obtain();
                Vector2 end = vectorPool.obtain();

                start.set(renderComponent.facingRight() ? 1 : -1, 1);
                end.set(renderComponent.facingRight() ? 1 : -1, -1);
                IntArray entities = getEntitiesInArea(start, end);
                for (int i = 0; i < entities.size; i++) {
                    int entity = entities.get(i);
                    applyDamage(entity, new Damage(Damage.DamageType.Normal,
                            5 + casterStat.getCurrentStat(StatComponent.BaseStat.attackDamage) * 1.1f,
                            0));
                }

                vectorPool.free(start);
                vectorPool.free(end);
                break;
            case ABILITY_2:
                ControlComponent controlComponent = getComponent(entityID, ControlComponent.class);
                if (checkRange(entityID, controlComponent.targetId, ABILITY_2_RANGE)) {
                    StatComponent statComponent = getComponent(controlComponent.targetId, StatComponent.class);
                    statComponent.damages.add(new Damage(Damage.DamageType.True, 10 + casterStat.getCurrentStat(StatComponent.BaseStat.spellPower) * .4f, 0));
                    statComponent.statusEffects.put(BLEED_NAME, bleed.tickHandler(
                            new StatusEffect.TickHandler() {
                                private float time;

                                @Override
                                public void onTick(float delta, StatComponent statComponent, StatusEffect effect) {
                                    if (time == -1)
                                        return;
                                    time += delta;
                                    if (time >= .5f || effect.remainingTime <= .5f) {
                                        statComponent.damages.add(new Damage(Damage.DamageType.Normal, statComponent.getCurrentStat(StatComponent.BaseStat.attackDamage), 0));
                                        time = effect.remainingTime <= .5f ? -1 : 0;
                                    }
                                }
                            }
                    ).build());
                }
                break;
            case ABILITY_3:

                break;
            case ABILITY_4:
                ability4_activate = true;
                break;
        }
    }

    @Override
    public void affectStats(StatComponent statComponent) {
        if (ability4_activate) {
            if (statComponent.statusEffects.containsKey(BOOST_NAME))
                statComponent.statusEffects.remove(BOOST_NAME);
            else
                statComponent.statusEffects.put(BOOST_NAME, boost.build());
            ability4_activate = false;
        }
    }
}
