package gg.al.prototype.logic.entity.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Thomas Neumann on 21.03.2017.
 */
public class Physics extends PooledComponent {

    public Body body;

    @Override
    protected void reset() {
        body = null;
    }
}
