package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import lombok.Getter;

/**
 * Created by Thomas Neumann on 31.03.2017.
 */
public abstract class Physic extends PooledComponent {
    @Getter
    protected Body body;

    @Override
    protected void reset() {
        body = null;
    }

    public abstract void setBody(Body body);

    public abstract BodyDef.BodyType getBodyType();
}
