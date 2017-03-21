package gg.al.logic.entity.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Thomas Neumann on 21.03.2017.
 */
public class Movement extends PooledComponent {

    public final Vector2 direction;
    public float time;
    public float stepTime;

    public Movement() {
        this.direction = new Vector2();
        this.time = 0;
        this.stepTime = 0;
    }

    @Override
    protected void reset() {
        direction.set(0, 0);
        time = 0;
        stepTime = 0;
    }
}
