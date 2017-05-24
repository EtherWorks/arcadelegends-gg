package gg.al.character;


import gg.al.logic.component.StatComponent;
import gg.al.logic.component.data.StatusEffect;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
public class Kevin extends Character {

    private StatusEffect.StatusEffectBuilder effect = StatusEffect.builder().effectTime(2).percentageStat(StatComponent.BaseStat.attackSpeed, .6f);
    private boolean abil1 = false;

    @Override
    protected void onTick(float delta) {

    }

    @Override
    protected void onCast(int abilityInd) {
        switch (abilityInd) {
            case ABILITY_1:
                abil1 = true;
                break;
        }
    }

    @Override
    public void affectStats(StatComponent statComponent) {
        if (abil1) {
            statComponent.statusEffects.put("KevBoost", effect.build());
            abil1 = false;
        }
    }
}
