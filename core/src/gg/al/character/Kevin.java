package gg.al.character;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import gg.al.game.AL;
import gg.al.graphics.renderer.CharacterRenderer;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
@Slf4j
public class Kevin extends Character {

    public float ABILITY_2_RANGE = 1.5f;
    public float ABILITY_3_BOOST = .6f;
    public String BLEED_NAME = "KevBleed";
    public String BOOST_NAME = "KevBoost";

    private StatusEffect.StatusEffectBuilder boost = StatusEffect.builder()
            .effectTime(0)
            .percentageStat(StatComponent.BaseStat.attackSpeed, ABILITY_3_BOOST)
            .percentageStat(StatComponent.BaseStat.armor, -.5f);
    private StatusEffect.StatusEffectBuilder bleed = StatusEffect.builder()
            .effectTime(5);
    private boolean ability4_activate = false;
    private Random random = new Random();

    @Override
    protected void onTick(float delta) {

    }


    protected boolean checkOnCast(int abilityInd) {
        switch (abilityInd) {
            case ABILITY_1:
                return true;
            case ABILITY_2:
                int targetId = getEntityAtMouse();
                if (targetId == -1)
                    return false;
                if (checkRange(entityID, targetId, ABILITY_2_RANGE)) {
                    extraData[ABILITY_2] = targetId;
                    return true;
                }
                break;
            case ABILITY_3:
                return true;
            case ABILITY_4:
                targetId = getEntityAtMouse();
                if (targetId == -1)
                    return false;
                extraData[ABILITY_4] = targetId;
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

                start.set(-1, -1);
                end.set(1, 1);
                IntArray entities = getEntitiesInArea(start, end);
                for (int i = 0; i < entities.size; i++) {
                    int entity = entities.get(i);
                    if (entity == entityID)
                        continue;
                    applyDamage(entity, new Damage(Damage.DamageType.Normal,
                            50 + 20 * casterStat.getRuntimeStat(StatComponent.RuntimeStat.ability_1_points)
                                    + casterStat.getCurrentStat(StatComponent.BaseStat.attackDamage) * (0.5f + 0.1f * casterStat.getRuntimeStat(StatComponent.RuntimeStat.ability_1_points)),
                            casterStat.getCurrentStat(StatComponent.BaseStat.armorPenetration)));
                }

                vectorPool.free(start);
                vectorPool.free(end);
                break;
            case ABILITY_2:
                int targetId = (int) extraData[ABILITY_2];
                StatComponent statComponent = getComponent(targetId, StatComponent.class);
                statComponent.damages.add(new Damage(Damage.DamageType.True, 50 + 10 * casterStat.getRuntimeStat(StatComponent.RuntimeStat.ability_2_points)
                        + casterStat.getCurrentStat(StatComponent.BaseStat.spellPower) * .5f, 0));
                final float tickDamage = 20 * casterStat.getRuntimeStat(StatComponent.RuntimeStat.ability_2_points) + casterStat.getCurrentStat(StatComponent.BaseStat.attackDamage) * 0.3f;
                final float tickPen = statComponent.getCurrentStat(StatComponent.BaseStat.armorPenetration);
                statComponent.statusEffects.put(BLEED_NAME, bleed.tickHandler(
                        new StatusEffect.TickHandler() {
                            private float time;

                            @Override
                            public void onTick(float delta, StatComponent statComponent, StatusEffect effect) {
                                if (time == -1)
                                    return;
                                time += delta;
                                if (time >= .5f || effect.remainingTime <= .5f) {
                                    statComponent.damages.add(
                                            new Damage(Damage.DamageType.Normal,
                                                    tickDamage, tickPen));
                                    time = effect.remainingTime <= .5f ? -1 : 0;
                                }
                            }
                        }
                ).build());
                break;
            case ABILITY_3:
                ability4_activate = true;
                break;
            case ABILITY_4:
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
                bCon.target = (int) extraData[ABILITY_4];
                bCon.maxDistance = 20;
                final float damage = 0.4f;
                final int caster = entityID;
                bCon.collisionCallback = (bullet, hit, bFix, hFix, contact) -> {
                    StatComponent hitStat = getComponent(hit, StatComponent.class);
                    if (hitStat != null && hit != caster) {
                        hitStat.damages.add(new Damage(Damage.DamageType.True, Damage.DamageCalculationType.MaxHealth, damage, 0));
                        getComponent(bullet, BulletComponent.class).delete = true;
                        bFix.getBody().setLinearVelocity(Vector2.Zero);
                    }
                    contact.setEnabled(false);
                };
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
        super.attack(enemyId);
        AL.getAudioManager().playSound("sword_" + (random.nextInt(3) + 1));
    }

    @Override
    protected void castBegin(int ability) {
        RenderComponent renderComponent = getComponent(entityID, RenderComponent.class);
        switch (ability) {
            case ABILITY_1:
                resetAttack();
                renderComponent.setRenderState(CharacterRenderer.PlayerRenderState.ABILITY_1);
                break;
            case ABILITY_2:
                resetAttack();
                renderComponent.setRenderState(CharacterRenderer.PlayerRenderState.ABILITY_2);
                break;
            case ABILITY_4:
                resetAttack();
                renderComponent.setRenderState(CharacterRenderer.PlayerRenderState.ABILITY_4);
                break;
        }
    }
}
