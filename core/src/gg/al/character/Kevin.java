package gg.al.character;

import gg.al.logic.component.StatComponent;
import gg.al.logic.component.data.StatusEffect;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
public class Kevin extends Character {

    private StatusEffect effect;

    @Override
    public void castAbility1() {
    /*
        PLAY ANIM
        ON KEYPOINT
        GET ENEMIES THREE TILES BEFORE
        APPLY DAMAGE
     */
    }

    @Override
    public void castAbility2() {
        effect = StatusEffect.builder().effectTime(2).percentageStat(StatComponent.BaseStat.attackSpeed, 1f).build();
    }

    @Override
    public void castAbility3() {

    }

    @Override
    public void castAbility4() {

    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public void affectStats(StatComponent statComponent) {
        if (effect != null) {
            statComponent.statusEffects.put("KevBoost", effect);
            effect = null;
        }
    }
}
