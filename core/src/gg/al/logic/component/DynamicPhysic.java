package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
public class DynamicPhysic extends PooledComponent {

    public Body body = null;

    @Override
    protected void reset() {
        body = null;
    }

    public void set(Body body) {
        this.body = body;
    }
}
