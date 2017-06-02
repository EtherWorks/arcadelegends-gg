package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import gg.al.character.Character;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.Template;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
public class CharacterComponent extends PooledComponent implements ITemplateable {
    public final Vector2 move;
    public final Vector2 stepMove;
    public final Vector2 startToEnd;
    public Character character;
    public int targetId;

    public CharacterComponent() {
        move = new Vector2();
        stepMove = new Vector2();
        startToEnd = new Vector2();
        targetId = -1;
    }

    @Override
    protected void reset() {
        character = null;
        move.setZero();
        stepMove.setZero();
        startToEnd.setZero();
        targetId = -1;
    }

    public float getRenderMultiplicator() {
        return character.getRenderMultiplicator();
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
