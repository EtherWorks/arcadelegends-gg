package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by Thomas Neumann on 29.05.2017.<br />
 * {@link com.artemis.Component} describing a bullet.
 */
public class BulletComponent extends PooledComponent {

    /**
     * Direction the bullet should go to.
     */
    public final Vector2 move;

    /**
     * Last position. Used for distance calculations
     */
    public final Vector2 old;

    /**
     * Maximum distance the bullet can fly before being destroyed.
     */
    public float maxDistance;

    /**
     * The currently flown distance.
     */
    public float currentDistance;

    /**
     * Flag describing whether the bullet should be deleted on the next {@link gg.al.logic.system.BulletSystem} step.
     */
    public boolean delete;

    /**
     * Target of the bullet. -1 for no target.
     */
    public int target;

    /**
     * Speed of the bullet.
     */
    public float speed;

    /**
     * Callback for a collision.
     */
    public OnCollisionCallback collisionCallback;

    /**
     * Callback for the deletion event of the bullet.
     */
    public OnDeleteCallback deleteCallback;

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
        collisionCallback = null;
        deleteCallback = null;
        delete = false;
    }

    @FunctionalInterface
    public interface OnCollisionCallback {
        void onCollision(int bullet, int hit, Fixture bulletFix, Fixture hitFix, Contact contact);
    }

    @FunctionalInterface
    public interface OnDeleteCallback {
        void onDelete();
    }
}
