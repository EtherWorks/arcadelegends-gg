package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import gg.al.logic.component.*;
import lombok.extern.slf4j.Slf4j;

import static gg.al.graphics.renderer.CharacterRenderer.PlayerRenderState.*;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
@Slf4j
public class CharacterSystem extends IteratingSystem {

    private final World world;
    private ComponentMapper<PositionComponent> mapperPosition;
    private ComponentMapper<PhysicComponent> mapperPhysicComponent;
    private ComponentMapper<StatComponent> mapperStatComponent;
    private ComponentMapper<RenderComponent> mapperRenderComponent;
    private ComponentMapper<CharacterComponent> mapperCharacterComponent;

    private Pool<Vector2> vectorPool;

    public CharacterSystem(World world) {
        super(Aspect.all(CharacterComponent.class, PositionComponent.class, PhysicComponent.class, StatComponent.class));
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
        CharacterComponent character = mapperCharacterComponent.get(entityId);
        StatComponent stats = mapperStatComponent.get(entityId);
        PhysicComponent physicComponent = mapperPhysicComponent.get(entityId);
        RenderComponent renderComponent = mapperRenderComponent.get(entityId);


        if (character.move.dst(pos.position) != 0 && physicComponent.body.getLinearVelocity().equals(Vector2.Zero) && !stats.getFlag(StatComponent.FlagStat.dead)) {
            if (stats.getRuntimeStat(StatComponent.RuntimeStat.actionPoints) >= 1 && (renderComponent == null || renderComponent.isInState(IDLE)
                    || renderComponent.isInState(ATTACK))) {
                //anfangen sich zu bewegen
                Vector2 dir = vectorPool.obtain();
                Vector2 speed = vectorPool.obtain();
                if (Math.abs(character.move.x - pos.position.x) > Math.abs(character.move.y - pos.position.y))
                    dir.set(Math.signum(character.move.x - pos.position.x), 0);
                else
                    dir.set(0, Math.signum(character.move.y - pos.position.y));

                if (renderComponent != null) {
                    renderComponent.setRenderState(dir.y == 0 ? MOVE_SIDE :
                            dir.y < 0 ? MOVE_DOWN : MOVE_UP);
                    if (dir.x != 0)
                        renderComponent.flipX = dir.x < 0;
                }
                physicComponent.body.setLinearVelocity(speed.set(dir).scl(stats.getCurrentStat(StatComponent.BaseStat.moveSpeed)));
                character.stepMove.set(dir.add(pos.position));
                character.startToEnd.set(dir.set(pos.position).sub(character.stepMove));
                vectorPool.free(dir);
                vectorPool.free(speed);
                stats.addRuntimeStat(StatComponent.RuntimeStat.actionPoints, -1);
                character.targetId = -1;
            }
        } else if (!physicComponent.body.getLinearVelocity().equals(Vector2.Zero)) {
            //Testen ob an oder über stepmove, dann stoppen
            Vector2 curr = vectorPool.obtain();

            curr.set(physicComponent.body.getPosition()).sub(character.stepMove);

            if (curr.dot(character.startToEnd) < 0) {
                physicComponent.body.setLinearVelocity(Vector2.Zero);
                physicComponent.body.setTransform(pos.position, physicComponent.body.getAngle());
                renderComponent.setRenderState(IDLE);
            }
            vectorPool.free(curr);
        }

        if (character.targetId == entityId)
            character.targetId = -1;
        else if (character.targetId != -1) {
            Vector2 vector = vectorPool.obtain();
            PositionComponent target = mapperPosition.get(character.targetId);
            if (Math.abs(vector.set(pos.position).dst(target.position)) <= stats.getCurrentStat(StatComponent.BaseStat.attackRange)) {
                character.character.startAttack();
                renderComponent.setRenderState(ATTACK);
            } else
                character.targetId = -1;
            vectorPool.free(vector);
        } else if (character.targetId == -1 && renderComponent.isInState(ATTACK))
            renderComponent.setRenderState(IDLE);


        character.character.tick(getWorld().getDelta());
    }

}
