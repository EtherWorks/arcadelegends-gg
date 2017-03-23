package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
public class Move extends PooledComponent{
    public final Vector2 moveTo = new Vector2();

    @Override
    protected void reset() {
        moveTo.set(0,0);
    }
}
