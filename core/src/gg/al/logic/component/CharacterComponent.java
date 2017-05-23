package gg.al.logic.component;

import com.artemis.PooledComponent;
import gg.al.character.Character;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.Template;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
public class CharacterComponent extends PooledComponent implements ITemplateable {
    public Character character;

    @Override
    protected void reset() {
        character = null;
    }

    @Override
    public Template getTemplate() {
        return new CharacterTemplate();
    }

    @Override
    public void fromTemplate(Template template) {

    }

    public static class CharacterTemplate extends Template
    {
        public String characterName = "";
    }
}
