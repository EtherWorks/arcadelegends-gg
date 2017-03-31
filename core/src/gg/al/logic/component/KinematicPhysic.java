package gg.al.logic.component;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class KinematicPhysic extends Physic {
    @Override
    public void setBody(Body body) {
        if (body.getType() != BodyDef.BodyType.KinematicBody)
            throw new IllegalArgumentException("Only KinematicBody type allowed");
        this.body = body;
    }

    @Override
    public BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.KinematicBody;
    }
}
