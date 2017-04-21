package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Input extends PooledComponent {

    public final Vector2 move;
    public double lastDist;

    public Input() {
        move = new Vector2();
        lastDist = Double.MAX_VALUE;
    }

    @Override
    protected void reset() {
        move.set(Vector2.Zero);
        lastDist = Double.MAX_VALUE;
    }

    public void set(int moveX, int moveY) {
        move.set(moveX, moveY);
    }
}
