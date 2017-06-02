package gg.al.character;

import com.badlogic.gdx.math.Vector2;
import gg.al.logic.component.BulletComponent;
import gg.al.logic.component.PhysicComponent;
import gg.al.logic.component.PositionComponent;
import gg.al.logic.component.StatComponent;
import gg.al.logic.component.data.Damage;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;

/**
 * Created by Thomas Neumann on 03.06.2017.
 */
public class Lucian extends Character {
    @Override
    protected void onTick(float delta) {

    }

    @Override
    protected boolean checkOnCast(int abilityInd) {
        return false;
    }

    @Override
    protected void onCast(int abilityInd) {

    }

    @Override
    public void affectStats(StatComponent statComponent) {

    }

    @Override
    public void attack(int enemyId) {
        StatComponent stats = getComponent(entityID, StatComponent.class);
        EntityArguments arguments = getArguments("bullet.json");
        PositionComponent.PositionTemplate pos = arguments.get(PositionComponent.class.getSimpleName(), PositionComponent.PositionTemplate.class);
        Vector2 enemyPos = getComponent(enemyId, PositionComponent.class).position;
        Vector2 charPos = getCharacterPosition();

        Vector2 dir = new Vector2(enemyPos).sub(charPos).nor();
        pos.x = charPos.x + dir.x;
        pos.y = charPos.y + dir.y;
        int entity = spawn(Entities.Bullet, arguments);
        BulletComponent bCon = getComponent(entity, BulletComponent.class);
        getComponent(entity, PhysicComponent.class).body.setBullet(true);
        bCon.speed = 10;
        bCon.move.set(dir);
        bCon.old.set(pos.x, pos.y);
        bCon.target = enemyId;
        bCon.maxDistance = Float.MAX_VALUE;
        final float damage = stats.getCurrentStat(StatComponent.BaseStat.attackDamage);
        final int caster = entityID;
        bCon.callback = (bullet, hit, bFix, hFix, contact) -> {
            getComponent(bullet, BulletComponent.class).delete = true;
            bFix.getBody().setLinearVelocity(Vector2.Zero);
            StatComponent statComponent = getComponent(hit, StatComponent.class);
            if (statComponent != null && hit != caster)
                statComponent.damages.add(new Damage(Damage.DamageType.Normal, damage, 0));
            contact.setEnabled(false);
        };
    }
}
