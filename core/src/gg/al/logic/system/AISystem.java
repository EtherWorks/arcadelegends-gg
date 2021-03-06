package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.character.Character;
import gg.al.graphics.renderer.CharacterRenderer;
import gg.al.logic.component.*;

/**
 * Created by Thomas Neumann on 12.06.2017.<br>
 * {@link IteratingSystem} responsible for controlling all entities with {@link AIComponent}.
 */
public class AISystem extends IteratingSystem {

    private ComponentMapper<CharacterComponent> mapperCharacterComponent;
    private ComponentMapper<AIComponent> mapperAIComponent;
    private ComponentMapper<StatComponent> mapperStatComponent;
    private ComponentMapper<PositionComponent> mapperPositionComponent;
    private ComponentMapper<RenderComponent> mapperRenderComponent;

    public AISystem() {
        super(Aspect.all(AIComponent.class, CharacterComponent.class));
    }

    @Override
    protected void process(int entityId) {
        AIComponent aIComponent = mapperAIComponent.get(entityId);
        CharacterComponent characterComponent = mapperCharacterComponent.get(entityId);
        StatComponent statComponent = mapperStatComponent.get(entityId);
        PositionComponent pos = mapperPositionComponent.get(entityId);

        if (statComponent.getRuntimeStat(StatComponent.RuntimeStat.health) != statComponent.getCurrentStat(StatComponent.BaseStat.maxHealth))
            aIComponent.aggroRange = 30;

        if (aIComponent.target != -1) {
            PositionComponent enemyPos = mapperPositionComponent.get(aIComponent.target);
            float distance = enemyPos.position.dst(pos.position);
            if (distance > aIComponent.aggroRange)
                return;

            if (characterComponent.character.getAIExtension() != null) {
                Character.AIExtension aiExtension = characterComponent.character.getAIExtension();

                for (int ability = 0; ability < 5; ability++) {
                    if (aiExtension.disabled(ability))
                        continue;
                    if (characterComponent.character.getCooldownTimer(ability) == 0 && distance <= aiExtension.getRange(ability)) {
                        characterComponent.character.cast(ability);
                    }
                }
            }
            if (!characterComponent.character.isCasting())
                if (distance > statComponent.getCurrentStat(StatComponent.BaseStat.attackRange)) {
                    characterComponent.move.set(enemyPos.position);
                    characterComponent.targetId = -1;
                } else {
                    characterComponent.move.set(pos.position);
                    if (!characterComponent.character.isCasting())
                        characterComponent.targetId = aIComponent.target;
                }


        }
    }
}
