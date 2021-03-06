package gg.al.character;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import gg.al.game.AL;
import gg.al.graphics.renderer.CharacterRenderer;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.Heal;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Created by Thomas Neumann on 23.05.2017.<br>
 * Main {@link Character} of the game.
 */
@Slf4j
public class Kevin extends Character {

    public static final String[] ICON_NAMES = {"trait", "ability1", "ability2", "ability3", "ability4"};
    public static final String[] ATTACK_SOUNDS = {"sword_1", "sword_2", "sword_3", "sword_4"};
    private static final Color INACTIVE_COLOR = new Color(.3f, .3f, .3f, .8f);
    public float ABILITY_2_RANGE = 1.5f;
    public String BLEED_NAME = "KevBleed";
    public String BOOST_NAME = "KevBoost";
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
                return !isMoving();
            case ABILITY_2:
                if (isMoving())
                    return false;
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
                if (isMoving())
                    return false;
                targetId = getEntityAtMouse();
                if (targetId == -1 || targetId == entityID)
                    return false;
                extraData[ABILITY_4] = targetId;
                return true;
            case TRAIT:
                return true;
        }
        return false;
    }

    @Override
    protected boolean checkOnTrigger(int abilityInd) {
        switch (abilityInd) {
            case ABILITY_1:
                return !isMoving();
            case ABILITY_2:
                if (isMoving())
                    return false;
                int targetId = (int) extraData[ABILITY_2];
                if (targetId == -1)
                    return false;
                if (checkRange(entityID, targetId, ABILITY_2_RANGE)) {
                    return true;
                }
                break;
            case ABILITY_3:
                return true;
            case ABILITY_4:
                if (isMoving())
                    return false;
                targetId = (int) extraData[ABILITY_4];
                return !(targetId == -1 || targetId == entityID);
            case TRAIT:
                return true;
        }
        return false;
    }

    @Override
    protected void onCast(int abilityInd) {

        StatComponent casterStat = getComponent(entityID, StatComponent.class);
        switch (abilityInd) {
            /**
             * Targets the ground around {@link Kevin}, and damages all nearby enemies.
             */
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
                    applyDamage(entity, new Damage(Damage.DamageType.Physical,
                            50 + 20 * casterStat.getRuntimeStat(StatComponent.RuntimeStat.ability_1_points)
                                    + casterStat.getCurrentStat(StatComponent.BaseStat.attackDamage) * (0.5f + 0.1f * casterStat.getRuntimeStat(StatComponent.RuntimeStat.ability_1_points)),
                            casterStat.getCurrentStat(StatComponent.BaseStat.armorPenetration)));
                }

                vectorPool.free(start);
                vectorPool.free(end);
                break;
            /**
             * Stabs the targeted entity, and applies a bleeding {@link StatusEffect}.
             */
            case ABILITY_2:
                int targetId = (int) extraData[ABILITY_2];
                StatComponent statComponent = getComponent(targetId, StatComponent.class);
                statComponent.damages.add(new Damage(Damage.DamageType.True, 50 + 10 * casterStat.getRuntimeStat(StatComponent.RuntimeStat.ability_2_points)
                        + casterStat.getCurrentStat(StatComponent.BaseStat.spellPower) * .5f, 0));
                final float tickDamage = 20 * casterStat.getRuntimeStat(StatComponent.RuntimeStat.ability_2_points) + casterStat.getCurrentStat(StatComponent.BaseStat.attackDamage) * 0.3f;
                final float tickPen = statComponent.getCurrentStat(StatComponent.BaseStat.armorPenetration);
                StatusEffect bleedStatus = bleed.build();
                bleedStatus.tickHandler = new StatusEffect.TickHandler() {
                    private float time;

                    @Override
                    public void onTick(float delta, StatComponent statComponent) {
                        if (time == -1)
                            return;
                        time += delta;
                        if (time >= .5f || bleedStatus.remainingTime <= .5f) {
                            statComponent.damages.add(
                                    new Damage(Damage.DamageType.Physical,
                                            tickDamage, tickPen));
                            time = bleedStatus.remainingTime <= .5f ? -1 : 0;
                        }
                    }
                };
                statComponent.statusEffects.put(BLEED_NAME, bleedStatus);
                break;
            /**
             * Grants a {@link StatusEffect} which strengths {@link Kevin}s offensive power, but decreases his defense.
             */
            case ABILITY_3:
                ability4_activate = true;
                break;
            /**
             * Fires a big rocket towards the targeted enemy, which deals splash damage.
             */
            case ABILITY_4:
                statComponent = getComponent(entityID, StatComponent.class);
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
                bCon.speed = 4;
                bCon.move.set(dir);
                bCon.old.set(pos.x, pos.y);
                bCon.target = (int) extraData[ABILITY_4];
                bCon.maxDistance = 10;
                final float damage = 0.4f + 0.1f * statComponent.getCurrentStat(StatComponent.BaseStat.attackDamage) / 100;
                final float baseDamage = 200 + 50 * statComponent.getRuntimeStat(StatComponent.RuntimeStat.ability_4_points);
                final int caster = entityID;
                bCon.collisionCallback = (bullet, hit, bFix, hFix, contact) -> {
                    StatComponent hitStat = getComponent(hit, StatComponent.class);
                    if (hitStat != null && hit != caster) {
                        hitStat.damages.add(new Damage(Damage.DamageType.True, baseDamage, 0));
                        hitStat.damages.add(new Damage(Damage.DamageType.True, Damage.DamageCalculationType.MissingHealth, damage, 0));
                        getComponent(bullet, BulletComponent.class).delete = true;
                        bFix.getBody().setLinearVelocity(Vector2.Zero);
                    }
                    contact.setEnabled(false);
                };
                bCon.deleteCallback = () ->
                {
                    IntArray enemies = getEntitiesInArea(getComponent(entity, PhysicComponent.class).body.getPosition(), new Vector2(-2, -2), new Vector2(2, 2));
                    for (int i = 0; i < enemies.size; i++) {
                        int enemy = enemies.get(i);
                        if (enemy == caster)
                            continue;
                        StatComponent enemyStat = getComponent(enemy, StatComponent.class);
                        if (enemyStat != null) {
                            enemyStat.damages.add(new Damage(Damage.DamageType.Physical, 300, 0));
                        }
                    }
                    AL.getAudioManager().playSound("boom");
                };

                AL.getAudioManager().playSound("rocketlauncher");
                break;
        }
    }

    @Override
    public void affectStats(StatComponent statComponent) {

        statComponent.addCurrentStat(StatComponent.BaseStat.cooldownAbility1, -statComponent.getRuntimeStat(StatComponent.RuntimeStat.ability_1_points));

        if (ability4_activate) {
            if (statComponent.statusEffects.containsKey(BOOST_NAME)) {
                statComponent.statusEffects.remove(BOOST_NAME);
            } else
                statComponent.statusEffects.put(BOOST_NAME,
                        StatusEffect.builder()
                                .percentageStat(StatComponent.BaseStat.moveSpeed, 1f)
                                .percentageStat(StatComponent.BaseStat.armor, -0.5f)
                                .percentageStat(StatComponent.BaseStat.magicResist, -0.5f)
                                .percentageStat(StatComponent.BaseStat.attackSpeed, 0.5f + 0.2f * statComponent.getRuntimeStat(StatComponent.RuntimeStat.ability_3_points))
                                .build());
            ability4_activate = false;
        }
        statComponent.addCurrentStat(StatComponent.BaseStat.attackSpeed,
                statComponent.getCurrentStat(StatComponent.BaseStat.attackSpeed) *
                        (1 + statComponent.getRuntimeStat(StatComponent.RuntimeStat.trait_points) * 0.2f) *
                        (1 - (statComponent.getRuntimeStat(StatComponent.RuntimeStat.health) / statComponent.getCurrentStat(StatComponent.BaseStat.maxHealth))));
    }

    @Override
    public void attack(int enemyId) {
        super.attack(enemyId);
        AL.getAudioManager().playSound(ATTACK_SOUNDS[random.nextInt(ATTACK_SOUNDS.length)]);
        StatComponent stat = getComponent(entityID, StatComponent.class);
        if (stat.statusEffects.containsKey(BOOST_NAME)) {
            if (stat.getRuntimeStat(StatComponent.RuntimeStat.resource) >= 5) {
                stat.heals.add(new Heal(25 * (1 + stat.getRuntimeStat(StatComponent.RuntimeStat.ability_3_points))));
                stat.addRuntimeStat(StatComponent.RuntimeStat.resource, -5);
            }
        }
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
                PositionComponent target = getComponent((Integer) extraData[ABILITY_2], PositionComponent.class);
                Vector2 pos = getCharacterPosition();
                if (pos.x < target.position.x)
                    renderComponent.faceRight();
                else if (pos.x > target.position.x)
                    renderComponent.faceLeft();
                break;
            case ABILITY_4:
                resetAttack();
                renderComponent.setRenderState(CharacterRenderer.PlayerRenderState.ABILITY_4);
                target = getComponent((Integer) extraData[ABILITY_4], PositionComponent.class);
                pos = getCharacterPosition();
                if (pos.x < target.position.x)
                    renderComponent.faceRight();
                else if (pos.x > target.position.x)
                    renderComponent.faceLeft();
                break;
        }
    }

    @Override
    public Color getAbilityOverlay(int ability) {
        StatComponent stat = getComponent(entityID, StatComponent.class);
        return ability == ABILITY_3 && !stat.statusEffects.containsKey(BOOST_NAME) ? INACTIVE_COLOR : null;
    }

    @Override
    protected float modifyCost(int ability, float cost) {
        /**
         * If the boost is active, deactivating shouldn't cost anything.
         */
        if (ability == ABILITY_3 && getComponent(entityID, StatComponent.class).statusEffects.containsKey(BOOST_NAME)) {
            cost = 0;
        }
        return cost;
    }

    @Override
    public String[] getIconNames() {
        return ICON_NAMES;
    }
}
