package gg.al.logic.entity.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Thomas Neumann on 21.03.2017.
 */
public class PositionUpdate extends PooledComponent {

    public final Vector2 futurePosition;

    public PositionUpdate() {
        this.futurePosition = new Vector2();
    }

    @Override
    protected void reset() {
        futurePosition.set(0, 0);
    }
}
