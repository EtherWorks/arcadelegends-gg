package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.Template;
import gg.al.logic.map.Tile;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 * {@link com.artemis.Component} containing the positional data and a reference to the current {@link Tile}
 */
public class PositionComponent extends PooledComponent implements ITemplateable {

    /**
     * Current position on the map.
     */
    public final Vector2 position = new Vector2();
    /**
     * Current occupied {@link Tile}.
     */
    public Tile tile;
    /**
     * Whether the position should be reset.
     */
    public boolean resetPos;

    @Override
    protected void reset() {
        position.set(0, 0);
        tile = null;
        resetPos = false;
    }

    public void set(float x, float y) {
        position.set(x, y);
    }

    public void set(Tile tile) {
        this.tile = tile;
    }

    public void set(Vector2 vec) {
        position.set(vec);
    }

    public void translate(Vector2 move) {
        position.add(move);
    }

    @Override
    public Template getDefaultTemplate() {
        return new PositionTemplate();
    }

    @Override
    public void fromTemplate(Template template) {
        PositionTemplate positionTemplate = (PositionTemplate) template;
        position.set(positionTemplate.x, positionTemplate.y);
    }

    /**
     * {@link Template} for {@link PositionComponent}.
     */
    public static class PositionTemplate extends Template {
        public float x;
        public float y;
    }

}
