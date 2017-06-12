package gg.al.character;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.IntArray;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;

/**
 * Created by Thomas Neumann on 03.06.2017.
 */
public class Ezreal extends Character {
    public static final String[] ICON_NAMES = {"trait", "ability1", "ability2", "ability3", "ability4"};
    private static float PASSIVE_DURATION = 5f;
    private static int PASSIVE_MAX_STACK = 5;
    private static float PASSIVE_ATTACKSPEED_PERCENT = .15f;
    private static float ABILITY_1_MAX_DISTANCE = 7;
    private static float ABILITY_2_MAX_DISTANCE = 5;
    private static float ABILITY_4_MAX_DISTANCE = 3;
    private static StatusEffect.StatusEffectBuilder ABILITY_2_BOOST = StatusEffect.builder().effectTime(2).percentageStat(StatComponent.BaseStat.attackSpeed, 0.3f);
    private int passive_stacks;
    private float passive_tick;

    @Override
    protected void onTick(float delta) {
        if (passive_tick != 0) {
            passive_tick -= delta;
            if (passive_tick <= 0) {
                passive_stacks = 0;
                passive_tick = 0;
            }
        }
    }

    private void addPassiveStack() {
        passive_tick = PASSIVE_DURATION;
        if (passive_stacks < PASSIVE_MAX_STACK)
            passive_stacks++;
    }

    @Override
    protected boolean checkOnCast(int abilityInd) {
        return true;
    }

    @Override
    public String[] getIconNames() {
        return ICON_NAMES;
    }

    @Override
    protected void onCast(int abilityInd) {
        switch (abilityInd) {
            case ABILITY_1:
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
                bCon.maxDistance = ABILITY_1_MAX_DISTANCE;
                final float damage = stats.getCurrentStat(StatComponent.BaseStat.attackDamage) * 1.1f;
                final int caster1 = entityID;
                bCon.collisionCallback = (bullet, hit, bFix, hFix, contact) -> {
                    if (hit == caster1)
                        return;
                    BulletComponent bulletComponent = getComponent(bullet, BulletComponent.class);
                    StatComponent hitStat = getComponent(hit, StatComponent.class);
                    if (hitStat != null) {
                        bulletComponent.delete = true;
                        hitStat.damages.add(new Damage(Damage.DamageType.Physical, damage, 0));
                        for (int i = 0; i < cooldownTimer.length; i++) {
                            if (cooldownTimer[i] != 0) {
                                cooldownTimer[i] -= 1.5f;
                                if (cooldownTimer[i] <= 0)
                                    cooldownTimer[i] = 0;
                            }
                        }
                        addPassiveStack();
                    }
                };
                break;
            case ABILITY_2:
                stats = getComponent(entityID, StatComponent.class);
                arguments = getArguments("ezreal_auto.json");
                pos = arguments.get(PositionComponent.class.getSimpleName(), PositionComponent.PositionTemplate.class);
                mousePos = getMousePos();
                charPos = getCharacterPosition();

                dir = new Vector2(mousePos).sub(charPos).nor();
                pos.x = charPos.x;
                pos.y = charPos.y;
                entity = spawn(Entities.Bullet, arguments);
                bCon = getComponent(entity, BulletComponent.class);
                bCon.speed = 10;
                bCon.move.set(dir);
                bCon.old.set(pos.x, pos.y);
                bCon.target = -1;
                bCon.maxDistance = ABILITY_2_MAX_DISTANCE;
                final float damage2 = stats.getCurrentStat(StatComponent.BaseStat.spellPower) * 0.5f;
                final int caster = entityID;
                bCon.collisionCallback = (bullet, hit, bFix, hFix, contact) -> {
                    StatComponent hitStat = getComponent(hit, StatComponent.class);
                    if (hitStat != null) {
                        if (hit == caster)
                            hitStat.statusEffects.put("Ezreal2Boost", ABILITY_2_BOOST.build());
                        else
                            hitStat.damages.add(new Damage(Damage.DamageType.Magic, damage2, 0));
                        addPassiveStack();
                    }
                };
                break;
            case ABILITY_3:
                stats = getComponent(entityID, StatComponent.class);
                arguments = getArguments("ezreal_auto.json");
                pos = arguments.get(PositionComponent.class.getSimpleName(), PositionComponent.PositionTemplate.class);
                mousePos = getMousePos();
                charPos = getCharacterPosition();

                dir = new Vector2(mousePos).sub(charPos).nor();
                pos.x = charPos.x;
                pos.y = charPos.y;
                entity = spawn(Entities.Bullet, arguments);
                bCon = getComponent(entity, BulletComponent.class);
                bCon.speed = 20;
                bCon.move.set(dir);
                bCon.old.set(pos.x, pos.y);
                bCon.target = -1;
                bCon.maxDistance = 100;
                final float damage3 = stats.getCurrentStat(StatComponent.BaseStat.spellPower) * 1.1f;
                final int caster3 = entityID;
                bCon.collisionCallback = new BulletComponent.OnCollisionCallback() {

                    private float currDamage = damage3;

                    @Override
                    public void onCollision(int bullet, int hit, Fixture bFix, Fixture hFix, Contact contact) {
                        if (hit == caster3)
                            return;
                        StatComponent hitStat = Ezreal.this.getComponent(hit, StatComponent.class);
                        if (hitStat != null) {
                            hitStat.damages.add(new Damage(Damage.DamageType.Magic, currDamage, 0));
                            currDamage -= currDamage <= damage3 * 0.2f ? 0 : damage3 * 0.2f;
                            addPassiveStack();
                        }
                    }
                };
                break;
            case ABILITY_4:
                Vector2 target = getRoundMousePos();
                PhysicComponent phys = getComponent(entityID, PhysicComponent.class);
                PositionComponent position = getComponent(entityID, PositionComponent.class);
                CharacterComponent characterComponent = getComponent(entityID, CharacterComponent.class);

                characterComponent.move.set(target);
                phys.body.setTransform(target.x, target.y, phys.body.getAngle());
                position.set(target);

                IntArray entities = getEntitiesInArea(new Vector2(-3, -3), new Vector2(3, 3));
                for (int i = 0; i < entities.size; i++) {
                    int entitiy = entities.get(i);
                    if (entitiy != entityID) {
                        stats = getComponent(entityID, StatComponent.class);
                        arguments = getArguments("ezreal_auto.json");
                        pos = arguments.get(PositionComponent.class.getSimpleName(), PositionComponent.PositionTemplate.class);
                        Vector2 enemyPos = getComponent(entitiy, PositionComponent.class).position;
                        charPos = getCharacterPosition();

                        dir = new Vector2(enemyPos).sub(charPos).nor();
                        pos.x = charPos.x + dir.x;
                        pos.y = charPos.y + dir.y;
                        int bulletSpawn = spawn(Entities.Bullet, arguments);
                        bCon = getComponent(bulletSpawn, BulletComponent.class);
                        bCon.speed = 10;
                        bCon.move.set(dir);
                        bCon.old.set(pos.x, pos.y);
                        bCon.target = entitiy;
                        bCon.maxDistance = Float.MAX_VALUE;
                        final float damage4 = stats.getCurrentStat(StatComponent.BaseStat.attackDamage) + 100;
                        final int caster4 = entityID;
                        bCon.collisionCallback = (bullet, hit, bFix, hFix, contact) -> {
                            BulletComponent bulletComponent = getComponent(bullet, BulletComponent.class);
                            if (bulletComponent.target == hit) {
                                bulletComponent.delete = true;
                                StatComponent statComponent = getComponent(hit, StatComponent.class);
                                if (statComponent != null) {
                                    statComponent.damages.add(new Damage(Damage.DamageType.Physical, damage4, 0));
                                    addPassiveStack();
                                }
                            }
                        };
                        break;
                    }
                }
                break;
            case TRAIT:
                passive_stacks = PASSIVE_MAX_STACK;
                passive_tick = PASSIVE_DURATION;
                break;
        }
    }

    @Override
    public void affectStats(StatComponent statComponent) {
        statComponent.addCurrentStat(StatComponent.BaseStat.attackSpeed,
                statComponent.getBaseStat(StatComponent.BaseStat.attackSpeed) * passive_stacks * PASSIVE_ATTACKSPEED_PERCENT);
    }

    @Override
    public void attack(int enemyId) {
        StatComponent stats = getComponent(entityID, StatComponent.class);
        EntityArguments arguments = getArguments("ezreal_auto.json");
        PositionComponent.PositionTemplate pos = arguments.get(PositionComponent.class.getSimpleName(), PositionComponent.PositionTemplate.class);
        Vector2 enemyPos = getComponent(enemyId, PositionComponent.class).position;
        Vector2 charPos = getCharacterPosition();

        Vector2 dir = new Vector2(enemyPos).sub(charPos).nor();
        pos.x = charPos.x + dir.x;
        pos.y = charPos.y + dir.y;
        int entity = spawn(Entities.Bullet, arguments);
        BulletComponent bCon = getComponent(entity, BulletComponent.class);
        bCon.speed = 10;
        bCon.move.set(dir);
        bCon.old.set(pos.x, pos.y);
        bCon.target = enemyId;
        bCon.maxDistance = Float.MAX_VALUE;
        final float damage = random.nextFloat() <= stats.getCurrentStat(StatComponent.BaseStat.criticalStrikeChance) ?
                stats.getCurrentStat(StatComponent.BaseStat.attackDamage) * (2 + stats.getCurrentStat(StatComponent.BaseStat.criticalStrikeDamage)) :
                stats.getCurrentStat(StatComponent.BaseStat.attackDamage);
        bCon.collisionCallback = (bullet, hit, bFix, hFix, contact) -> {
            BulletComponent bulletComponent = getComponent(bullet, BulletComponent.class);
            if (bulletComponent.target == hit) {
                bulletComponent.delete = true;
                StatComponent statComponent = getComponent(hit, StatComponent.class);
                if (statComponent != null)
                    statComponent.damages.add(new Damage(Damage.DamageType.Physical, damage, 0));
            }
        };
    }

    @Override
    protected void castBegin(int ability) {
        if (ability != TRAIT)
            resetAttack();
    }

}
