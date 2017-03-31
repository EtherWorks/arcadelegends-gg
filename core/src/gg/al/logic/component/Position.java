package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import gg.al.logic.map.Tile;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
public class Position extends PooledComponent{

    public final Vector2 position = new Vector2();
    public Tile tile;

    @Override
    protected void reset() {
        position.set(0,0);
        tile = null;
    }

    public void set(float x, float y)
    {
        position.set(x,y);
    }

    public void set(Tile tile){
        this.tile = tile;
    }

    public void set(Vector2 vec) {
        position.set(vec);
    }

    public void translate(Vector2 move)
    {
        position.add(move);
    }
}
