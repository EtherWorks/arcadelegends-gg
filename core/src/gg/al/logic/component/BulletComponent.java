package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by Thomas Neumann on 29.05.2017.<br />
 */
public class BulletComponent extends PooledComponent {

    public final Vector2 move;

    public final Vector2 old;

    public float maxDistance;

    public float currentDistance;

    public boolean delete;

    public OnCollisionCallback callback;

    public BulletComponent() {
        move = new Vector2();
        old = new Vector2();
    }

    @Override
    protected void reset() {
        move.setZero();
        old.setZero();
        maxDistance = 0;
        currentDistance = 0;
        callback = null;
        delete = false;
    }

    @FunctionalInterface
    public interface OnCollisionCallback {
        void onCollision(int bullet, int hit, Fixture bulletFix, Fixture hitFix, Contact contact);
    }
}
