package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Damage;
import lombok.extern.slf4j.Slf4j;

import static gg.al.graphics.renderer.CharacterRenderer.PlayerRenderState.*;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
@Slf4j
public class CharacterControlSystem extends IteratingSystem {

    private final World world;
    private ComponentMapper<PositionComponent> mapperPosition;
    private ComponentMapper<CharacterControlComponent> mapperInput;
    private ComponentMapper<PhysicComponent> mapperPhysicComponent;
    private ComponentMapper<StatComponent> mapperStatComponent;
    private ComponentMapper<RenderComponent> mapperRenderComponent;
    private ComponentMapper<CharacterComponent> mapperCharacterComponent;

    private Pool<Vector2> vectorPool;

    public CharacterControlSystem(World world) {
        super(Aspect.all(CharacterControlComponent.class, PositionComponent.class, PhysicComponent.class, StatComponent.class));
        this.world = world;
        vectorPool = new Pool<Vector2>() {
            @Override
            protected Vector2 newObject() {
                return new Vector2();
            }

            @Override
            protected void reset(Vector2 vector) {
                vector.set(0, 0);
            }
        };
    }


    @Override
    protected void process(int entityId) {
        PositionComponent pos = mapperPosition.get(entityId);
        CharacterControlComponent input = mapperInput.get(entityId);
        CharacterComponent characterComponent = mapperCharacterComponent.get(entityId);
        StatComponent stats = mapperStatComponent.get(entityId);
        PhysicComponent physicComponent = mapperPhysicComponent.get(entityId);
        RenderComponent renderComponent = mapperRenderComponent.get(entityId);


        if (input.move.dst(pos.position) != 0 && physicComponent.body.getLinearVelocity().equals(Vector2.Zero) && !stats.getFlag(StatComponent.FlagStat.dead)) {
            if (stats.getRuntimeStat(StatComponent.RuntimeStat.actionPoints) >= 1 && (renderComponent == null || renderComponent.isInState(IDLE)
                    || renderComponent.isInState(ATTACK))) {
                //anfangen sich zu bewegen
                Vector2 dir = vectorPool.obtain();
                Vector2 speed = vectorPool.obtain();
                if (Math.abs(input.move.x - pos.position.x) > Math.abs(input.move.y - pos.position.y))
                    dir.set(Math.signum(input.move.x - pos.position.x), 0);
                else
                    dir.set(0, Math.signum(input.move.y - pos.position.y));

                if (renderComponent != null) {
                    renderComponent.setRenderState(dir.y == 0 ? MOVE_SIDE :
                            dir.y < 0 ? MOVE_DOWN : MOVE_UP);
                    renderComponent.flipX = dir.x < 0;
                }
                physicComponent.body.setLinearVelocity(speed.set(dir).scl(stats.getCurrentStat(StatComponent.BaseStat.moveSpeed)));
                input.stepMove.set(dir.add(pos.position));
                input.startToEnd.set(dir.set(pos.position).sub(input.stepMove));
                vectorPool.free(dir);
                vectorPool.free(speed);
                stats.addRuntimeStat(StatComponent.RuntimeStat.actionPoints, -1);
                stats.setRuntimeStat(StatComponent.RuntimeStat.attackSpeedTimer, 0);
                input.targetId = -1;
            }
        } else if (!physicComponent.body.getLinearVelocity().equals(Vector2.Zero)) {
            //Testen ob an oder über stepmove, dann stoppen
            Vector2 curr = vectorPool.obtain();

            curr.set(physicComponent.body.getPosition()).sub(input.stepMove);

            if (curr.dot(input.startToEnd) < 0) {
                physicComponent.body.setLinearVelocity(Vector2.Zero);
                physicComponent.body.setTransform(pos.position, physicComponent.body.getAngle());
                renderComponent.setRenderState(IDLE);
            }
            vectorPool.free(curr);
        }

        if (input.targetId == entityId)
            input.targetId = -1;
        else if (input.targetId != -1) {
            PositionComponent otherPos = mapperPosition.get(input.targetId);
            if (otherPos != null) {
                Vector2 vector = vectorPool.obtain();
                if (Math.abs(vector.set(pos.position).dst(otherPos.position)) <= stats.getCurrentStat(StatComponent.BaseStat.attackRange)) {
                    if (renderComponent != null)
                        renderComponent.setRenderState(ATTACK);
                    if (stats.getRuntimeStat(StatComponent.RuntimeStat.attackSpeedTimer) >= 1 / stats.getCurrentStat(StatComponent.BaseStat.attackSpeed)) {
                        stats.setRuntimeStat(StatComponent.RuntimeStat.attackSpeedTimer, 0);
                        if (characterComponent.character != null)
                            characterComponent.character.attack(input.targetId);
                        else {
                            Damage dmg = new Damage(Damage.DamageType.Normal,
                                    stats.getCurrentStat(StatComponent.BaseStat.attackDamage),
                                    stats.getCurrentStat(StatComponent.BaseStat.armorPenetration));
                            mapperStatComponent.get(input.targetId).damages.add(dmg);
                        }
                    } else
                        stats.addRuntimeStat(StatComponent.RuntimeStat.attackSpeedTimer, getWorld().getDelta());
                } else {
                    stats.setRuntimeStat(StatComponent.RuntimeStat.attackSpeedTimer, 0);
                    input.targetId = -1;
                }
                vectorPool.free(vector);
            } else {
                input.targetId = -1;
                stats.setRuntimeStat(StatComponent.RuntimeStat.attackSpeedTimer, 0);
                if (renderComponent != null)
                    renderComponent.setRenderState(IDLE);
            }
        }

    }

}
