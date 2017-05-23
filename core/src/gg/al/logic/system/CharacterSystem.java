package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import gg.al.logic.component.CharacterComponent;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
public class CharacterSystem extends IteratingSystem {

    private ComponentMapper<CharacterComponent> mapperCharacterComponent;

    public CharacterSystem() {
        super(Aspect.all(CharacterComponent.class));
    }

    @Override
    protected void process(int entityId) {
        CharacterComponent characterComponent = mapperCharacterComponent.get(entityId);
        characterComponent.character.tick(getWorld().getDelta());
    }
}
