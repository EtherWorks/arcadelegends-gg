package gg.al.logic.data;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by Thomas Neumann on 07.04.2017.<br />
 */
public interface IPhysic {
      BodyDef.BodyType getBodyType();

      Body getBody();

    void setBody(Body body);
}
