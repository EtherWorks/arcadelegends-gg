package gg.al.character;

import com.badlogic.gdx.math.Vector2;
import gg.al.graphics.renderer.CharacterRenderer;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Damage;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;

/**
 * Created by Thomas Neumann on 09.06.2017.<br />
 * Standard enemy of the game.
 */
public class Ghost extends Character {

    private AIExtension aiExtension = new AIExtension() {
        @Override
        public float getRange(int ability) {
            return ability == 1 ? 5 : 0;
        }

        @Override
        public boolean disabled(int ability) {
            return ability != 1;
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
        }
    }

    @Override
    protected void onTick(float delta) {

    }

    @Override
    protected boolean checkOnCast(int abilityInd) {
        return abilityInd == ABILITY_1 && !isMoving();
    }

    @Override
    protected boolean checkOnTrigger(int abilityInd) {
        return checkOnCast(abilityInd);
    }

    @Override
    protected void onCast(int abilityInd) {
        /**
         * Spawns a linear moving projectile.
         */
        if (abilityInd == ABILITY_1) {
            StatComponent stats = getComponent(entityID, StatComponent.class);
            EntityArguments arguments = getArguments("ezreal_auto.json");
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
            final float damage = stats.getCurrentStat(StatComponent.BaseStat.spellPower);
            final int caster1 = entityID;
            bCon.collisionCallback = (bullet, hit, bFix, hFix, contact) -> {
                if (hit == caster1 || hit != aiComponent.target)
                    return;
                BulletComponent bulletComponent = getComponent(bullet, BulletComponent.class);
                StatComponent hitStat = getComponent(hit, StatComponent.class);
                if (hitStat != null) {
                    bulletComponent.delete = true;
                    hitStat.damages.add(new Damage(Damage.DamageType.Magic, damage, 0));
                }
            };
        }
    }

    @Override
    public void affectStats(StatComponent statComponent) {

    }
}
