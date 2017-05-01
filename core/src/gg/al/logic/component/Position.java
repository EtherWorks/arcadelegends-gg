package gg.al.logic.component;

import com.badlogic.gdx.math.Vector2;
import gg.al.logic.data.IComponentDef;
import gg.al.logic.map.Tile;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
public class Position extends PooledDefComponent {


    public final Vector2 position = new Vector2();
    public Tile tile;
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
    public IComponentDef getDefaultDef() {
        return new PositionDef();
    }

    @Override
    public void fromDef(IComponentDef def) {
        PositionDef positionDef = (PositionDef) def;
        position.set(positionDef.x, positionDef.y);
    }

    public static class PositionDef extends IComponentDef
    {
        public int x;
        public int y;
    }

}
