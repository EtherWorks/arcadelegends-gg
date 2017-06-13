package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import gg.al.character.Character;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.Template;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 * {@link com.artemis.Component} containing data for controlling a {@link Character}. <br>
 * Also holds a reference to the {@link Character} the entity possess.
 */
public class CharacterComponent extends PooledComponent implements ITemplateable {
    /**
     * Target position for the movement system.
     */
    public final Vector2 move;
    public final Vector2 stepMove;
    public final Vector2 startToEnd;
    /**
     * Reference to the {@link Character} of this entity.
     */
    public Character character;
    /**
     * Target id of the auto-attack
     */
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

    /**
     * @see Character#getAttackCompletionPercent()
     */
    public float getRenderMultiplicator() {
        return character.getAttackCompletionPercent();
    }

    @Override
    public Template getDefaultTemplate() {
        return new CharacterTemplate();
    }

    @Override
    public void fromTemplate(Template template) {

    }

    /**
     * {@link Template} dor {@link CharacterComponent}.
     */
    public static class CharacterTemplate extends Template {
        public String characterName = "";
    }
}
