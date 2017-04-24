package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Input extends PooledComponent {

    public final Vector2 move;
    public final Vector2 stepMove;

    public final Vector2 startToEnd;

    public Input() {
        move = new Vector2();
        stepMove = new Vector2();
        startToEnd = new Vector2();
    }

    @Override
    protected void reset() {
        move.setZero();
        stepMove.setZero();
        startToEnd.setZero();
    }

    public void set(int moveX, int moveY) {
        move.set(moveX, moveY);
    }
}
