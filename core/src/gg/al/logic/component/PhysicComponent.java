package gg.al.logic.component;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.Template;

/**
 * Created by Thomas Neumann on 08.05.2017.<br>
 * {@link Component} containing physic related data, mainly the {@link Body} of the entity.
 */
public class PhysicComponent extends Component implements ITemplateable {

    /**
     * {@link Body} of the entity.
     */
    public Body body;

    @Override
    public Template getDefaultTemplate() {
        return new PhysicTemplate();
    }

    @Override
    public void fromTemplate(Template template) {

    }

    /**
     * {@link Template} for the {@link PhysicComponent}, containing data for initializing the {@link Body}.
     */
    public static class PhysicTemplate extends Template {
        public BodyDef.BodyType bodyType = BodyDef.BodyType.DynamicBody;
        public float radius = .47f;
        public CollisionCategory collisionCategory;
        public boolean isBullet = false;
        public boolean isSensor = false;

        public enum CollisionCategory {
            CHARACTER,
            PROJECTILE;

            public short getCategory() {
                switch (this) {
                    case CHARACTER:
                        return 0x0001;
                    case PROJECTILE:
                        return 0x0002;
                }
                throw new IllegalArgumentException("Wat in CollisionCategory");
            }

            public short getMask() {
                switch (this) {
                    case CHARACTER:
                        return (short) (CHARACTER.getCategory() | PROJECTILE.getCategory());
                    case PROJECTILE:
                        return CHARACTER.getCategory();
                }
                throw new IllegalArgumentException("Wat in CollisionCategory");
            }
        }
    }


}
