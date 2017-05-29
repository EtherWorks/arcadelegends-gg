package gg.al.logic.component;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.Template;

/**
 * Created by Thomas Neumann on 08.05.2017.<br />
 */
public class PhysicComponent extends Component implements ITemplateable {
    public Body body;

    @Override
    public Template getTemplate() {
        return new PhysicTemplate();
    }

    @Override
    public void fromTemplate(Template template) {

    }

    public static class PhysicTemplate extends Template {
        public BodyDef.BodyType bodyType = BodyDef.BodyType.DynamicBody;
        public float radius = .47f;
    }
}
