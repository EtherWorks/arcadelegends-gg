package gg.al.character;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import gg.al.graphics.renderer.CharacterRenderer;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Damage;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;

/**
 * Created by Thomas Neumann on 09.06.2017.<br>
 * First boss of the game.
 */
public class SuperGhost extends Character {

    private AIExtension aiExtension = new AIExtension() {
        @Override
        public float getRange(int ability) {
            switch (ability) {
                case ABILITY_1:
                    return 5;
                case ABILITY_4:
                    return 3;
                default:
                    return 0;
            }
        }

        @Override
        public boolean disabled(int ability) {
            return ability != ABILITY_1 && ability != ABILITY_4;
        }
    };

    @Override
    public AIExtension getAIExtension() {
        return aiExtension;
    }

    @Override
    protected void castBegin(int ability) {
        RenderComponent renderComponent = getComponent(entityID, RenderComponent.class);
        switch (ability) {
            case ABILITY_1:
                renderComponent.setRenderState(CharacterRenderer.PlayerRenderState.ABILITY_1);
                break;
            case ABILITY_4:
                renderComponent.setRenderState(CharacterRenderer.PlayerRenderState.ABILITY_4);
                break;
        }
    }

    @Override
    protected void onTick(float delta) {

    }

    @Override
    protected boolean checkOnCast(int abilityInd) {
        switch (abilityInd) {
            case ABILITY_1:
                return !isMoving();
            case ABILITY_4:
                return !isMoving();
            default:
                return false;
        }
    }

    @Override
    protected boolean checkOnTrigger(int abilityInd) {
        return checkOnCast(abilityInd);
    }

    @Override
    protected void onCast(int abilityInd) {
        switch (abilityInd) {
            /**
             * Spawns a linear moving projectile.
             */
            case ABILITY_1:
                StatComponent stats = getComponent(entityID, StatComponent.class);
                EntityArguments arguments = getArguments("superghost_auto.json");
                PositionComponent.PositionTemplate pos = arguments.get(PositionComponent.class.getSimpleName(), PositionComponent.PositionTemplate.class);
                AIComponent aiComponent = getComponent(entityID, AIComponent.class);

                Vector2 enemyPos = getComponent(aiComponent.target, PositionComponent.class).position;
                Vector2 charPos = getCharacterPosition();

                Vector2 dir = new Vector2(enemyPos).sub(charPos).nor();
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
                    if (hit == caster1 || hit != aiComponent.target)
                        return;
                    BulletComponent bulletComponent = getComponent(bullet, BulletComponent.class);
                    StatComponent hitStat = getComponent(hit, StatComponent.class);
                    if (hitStat != null) {
                        bulletComponent.delete = true;
                        hitStat.damages.add(new Damage(Damage.DamageType.Physical, damage, 0));
                    }
                };
                break;
            /**
             * Slams the ground around {@link SuperGhost}, dealing massive damage to all entities.
             */
            case ABILITY_4:
                IntArray entities = getEntitiesInArea(new Vector2(-2, -2), new Vector2(2, 2));
                for (int i = 0; i < entities.size; i++) {
                    int enemy = entities.get(i);
                    if (enemy == entityID)
                        continue;
                    StatComponent statComponent = getComponent(enemy, StatComponent.class);
                    if (statComponent != null) {
                        statComponent.damages.add(new Damage(Damage.DamageType.Magic, 150, 0));
                    }
                }
                break;
        }
    }

    @Override
    public void affectStats(StatComponent statComponent) {

    }
}
