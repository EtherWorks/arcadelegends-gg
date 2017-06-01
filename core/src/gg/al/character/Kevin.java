package gg.al.character;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;
import gg.al.logic.map.Tile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
@Slf4j
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


    protected boolean checkOnCast(int abilityInd) {
        switch (abilityInd) {
            case ABILITY_1:
                return true;
            case ABILITY_2:
                Vector2 mousePos = getMousePos();
                Tile t = getTile(mousePos);
                if (t.getEntities().size == 0)
                    return false;
                int targetId = t.getEntities().first();
                if (checkRange(entityID, targetId, ABILITY_2_RANGE)) {
                    extraData[ABILITY_2] = targetId;
                    return true;
                }
            case ABILITY_3:
                return true;
            case ABILITY_4:
                return true;
            case TRAIT:
                return true;
        }
        return false;
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
                int targetId = (int) extraData[ABILITY_2];
                StatComponent statComponent = getComponent(targetId, StatComponent.class);
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
                break;
            case ABILITY_3:
                EntityArguments arguments = getArguments("bullet.json");
                PositionComponent.PositionTemplate pos = arguments.get(PositionComponent.class.getSimpleName(), PositionComponent.PositionTemplate.class);
                Vector2 mousePos = getMousePos();
                Vector2 charPos = getCharacterPosition();

                Vector2 dir = new Vector2(mousePos).sub(charPos).nor();
                pos.x = charPos.x + dir.x;
                pos.y = charPos.y + dir.y;
                int entity = spawn(Entities.Bullet, arguments);
                BulletComponent bCon = getComponent(entity, BulletComponent.class);
                getComponent(entity, PhysicComponent.class).body.setBullet(true);
                bCon.speed = 2;
                bCon.move.set(dir);
                bCon.old.set(pos.x, pos.y);
                bCon.target = 1;
                bCon.maxDistance = 20;
                final float damage = 600;
                final int caster = entityID;
                bCon.callback = (bullet, hit, bFix, hFix, contact) -> {
                    StatComponent hitStat = getComponent(hit, StatComponent.class);
                    if (hitStat != null && hit != caster) {
                        hitStat.damages.add(new Damage(Damage.DamageType.Normal, damage, 0));
                        getComponent(bullet, BulletComponent.class).delete = true;
                        bFix.getBody().setLinearVelocity(Vector2.Zero);
                    }
                    contact.setEnabled(false);
                };
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
        statComponent.addCurrentStat(StatComponent.BaseStat.attackSpeed,
                statComponent.getCurrentStat(StatComponent.BaseStat.attackSpeed) * 2 *
                        (1 - (statComponent.getRuntimeStat(StatComponent.RuntimeStat.health) / statComponent.getCurrentStat(StatComponent.BaseStat.maxHealth))));
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
