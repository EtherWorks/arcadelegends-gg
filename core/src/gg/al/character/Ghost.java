package gg.al.character;

import com.badlogic.gdx.math.Vector2;
import gg.al.logic.component.BulletComponent;
import gg.al.logic.component.PositionComponent;
import gg.al.logic.component.StatComponent;
import gg.al.logic.component.data.Damage;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;

/**
 * Created by Thomas Neumann on 09.06.2017.<br />
 */
public class Ghost extends Character {


    @Override
    protected void castBegin(int ability) {

    }

    @Override
    protected void onTick(float delta) {

    }

    @Override
    protected boolean checkOnCast(int abilityInd) {
        if (abilityInd == ABILITY_1) {

            return true;
        } else
            return false;
    }

    @Override
    protected void onCast(int abilityInd) {
        if (abilityInd == ABILITY_1) {
            StatComponent stats = getComponent(entityID, StatComponent.class);
            EntityArguments arguments = getArguments("ezreal_auto.json");
            PositionComponent.PositionTemplate pos = arguments.get(PositionComponent.class.getSimpleName(), PositionComponent.PositionTemplate.class);
            Vector2 mousePos = getMousePos();
            Vector2 charPos = getCharacterPosition();

            Vector2 dir = new Vector2(mousePos).sub(charPos).nor();
            pos.x = charPos.x;
            pos.y = charPos.y;
            int entity = spawn(Entities.Bullet, arguments);
            BulletComponent bCon = getComponent(entity, BulletComponent.class);
            bCon.speed = 10;
            bCon.move.set(dir);
            bCon.old.set(pos.x, pos.y);
            bCon.target = -1;
            bCon.maxDistance = 5;
            final float damage = stats.getCurrentStat(StatComponent.BaseStat.attackDamage) * 1.1f;
            final int caster1 = entityID;
            bCon.collisionCallback = (bullet, hit, bFix, hFix, contact) -> {
                if (hit == caster1)
                    return;
                BulletComponent bulletComponent = getComponent(bullet, BulletComponent.class);
                StatComponent hitStat = getComponent(hit, StatComponent.class);
                if (hitStat != null) {
                    bulletComponent.delete = true;
                    hitStat.damages.add(new Damage(Damage.DamageType.Normal, damage, 0));
                }
            };
        }
    }

    @Override
    public void affectStats(StatComponent statComponent) {

    }
}
