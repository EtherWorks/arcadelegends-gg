package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import lombok.Getter;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
public class DynamicPhysic extends PooledComponent implements IPhysic {

    protected Body body;

    @Override
    protected void reset() {
        body = null;
    }

    @Override
    public void setBody(Body body) {
        if (body.getType() != BodyDef.BodyType.DynamicBody)
            throw new IllegalArgumentException("Only DynamicBody type allowed");
        this.body = body;
    }

    @Override
    public BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.DynamicBody;
    }

    @Override
    public Body getBody() {
        return body;
    }
}
